package com.app.backend.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.app.backend.entity.Room;
import com.app.backend.entity.User;
import lombok.RequiredArgsConstructor;
import com.app.backend.constant.RedisKey;
import com.app.backend.service.CacheService;
import com.app.backend.service.RedisService;
import java.time.Duration;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.app.backend.exception.CommonException;
import com.app.backend.exception.ErrorCode;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.entity.ManagerGroup;
import com.app.backend.repository.ManagerGroupRepository;
import com.app.backend.service.intf.ManagerGroupService;

@Service
@Transactional
@RequiredArgsConstructor
public class ManagerGroupServiceImpl implements ManagerGroupService {
    private final ManagerGroupRepository repo;
    private final CacheService cacheService;
    private final UserServiceImpl userService;
    private final RoomServiceImpl roomService;


    private static final Duration MANAGERGROUP_CACHE_TTL = Duration.ofMinutes(15);

    public ManagerGroupResponse mapToResponse(ManagerGroup entity) {
        ManagerGroupResponse resp = new ManagerGroupResponse();

        resp.setId(entity.getId());
        resp.setName(entity.getName());

        resp.setListRoom(entity.getRooms().stream().map(Room::getId).toList());
        resp.setListUser(entity.getUsers().stream().map(User::getId).toList());
        return resp;
    }


    private void addRoomToGroupAndSave(ManagerGroup entity, List<Room> rooms){
        for (Room room: rooms){
            if(!entity.getRooms().contains(room)){
                entity.getRooms().add(room);
                room.setManagerGroup(entity);
                roomService.updateManager(room, entity);
            }
        }
    }

    private void removeUserToGroup(ManagerGroup entity, User user){
        entity.getUsers().remove(user);
        user.setManagerGroup(null);
    }

    private void addUserToGroupAndSave(ManagerGroup entity, List<User> users){
        for (User user: users){
            if(!entity.getUsers().contains(user)){
                entity.getUsers().add(user);
                user.setManagerGroup(entity);
                userService.changeManageGroup(user, entity);
            }
        }
    }

    private void removeRoomToGroup(ManagerGroup entity, User user){
        entity.getUsers().remove(user);
        user.setManagerGroup(null);
    }

    @Override
    public ManagerGroup create(ManagerGroupRequest request) {
        ManagerGroup entity = new ManagerGroup();
        entity.setName(request.getName());

//        addUserToGroupAndSave(entity, request.getListUser().stream().map(userService::getById).toList());
//        addRoomToGroupAndSave(entity, request.getListRoom().stream().map(roomService::getById).toList());
        entity.setRooms(request.getListRoom().stream().map(roomService::getById).toList());
        entity.setUsers(request.getListUser().stream().map(userService::getById).toList());

        entity = repo.save(entity);
        evictTopicCache(null);
        return entity;
    }

    @Override
    public ManagerGroup getById(Integer id) {
        return cacheService.getOrLoad(
                RedisKey.managerGroupById(id),
                ManagerGroup.class,
                MANAGERGROUP_CACHE_TTL,
                () -> repo.findById(id).orElseThrow(() -> new CommonException(ErrorCode.MANAGER_GROUP_NOT_FOUND))
        );
    }

    @Override
    public List<ManagerGroup> getAll() {
        ManagerGroup[] items = cacheService.getOrLoad(
                RedisKey.managerGroupAll(),
                ManagerGroup[].class,
                MANAGERGROUP_CACHE_TTL,
                () -> repo.findAll().toArray(new ManagerGroup[0])
        );
        return Arrays.asList(items);
    }

    @Override
    public ManagerGroup update(Integer id, ManagerGroupRequest request) {
        ManagerGroup entity = repo.findById(id).orElseThrow(() -> new CommonException(ErrorCode.ROLE_NOT_FOUND));
        entity.setName(request.getName());
//        entity.setRooms(request.getListRoom().stream().map(roomService::getById).toList());
//        entity.setUsers(request.getListUser().stream().map(userService::getById).toList());
        addUserToGroupAndSave(entity, request.getListUser().stream().map(userService::getById).toList());
        addRoomToGroupAndSave(entity, request.getListRoom().stream().map(roomService::getById).toList());
        entity = repo.save(entity);
        evictTopicCache(null);
        return entity;
    }

    
    private void evictTopicCache(Integer topicId) {
        try {
            cacheService.evict(RedisKey.managerGroupAll());
            if (topicId != null) {
                cacheService.evict(RedisKey.managerGroupById(topicId));
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public void delete(Integer id) { evictTopicCache(id);
        repo.deleteById(id); }
}
