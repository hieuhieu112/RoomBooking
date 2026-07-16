package com.app.backend.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.app.backend.entity.Room;
import lombok.RequiredArgsConstructor;
import com.app.backend.constant.RedisKey;
import com.app.backend.service.CacheService;
import com.app.backend.service.RedisService;
import java.time.Duration;
import java.util.Arrays;
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
//    private final UserServiceImpl userService;
    private final RoomServiceImpl roomService;


    private static final Duration MANAGERGROUP_CACHE_TTL = Duration.ofMinutes(15);
    private final RedisService redisService;

    public ManagerGroupResponse mapToResponse(ManagerGroup entity) {
        ManagerGroupResponse resp = new ManagerGroupResponse();

        resp.setId(entity.getId());
        resp.setName(entity.getName());
        return resp;
    }

    @Override
    public ManagerGroup create(ManagerGroupRequest request) {
        ManagerGroup entity = new ManagerGroup();
        entity.setName(request.getName());


        entity.setRooms(request.getListRoom().stream().map(roomService::getById).toList());
//        entity.setUsers(request.getListUser().stream().map(userService::getById).toList());

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
        entity.setRooms(request.getListRoom().stream().map(roomService::getById).toList());
//        entity.setUsers(request.getListUser().stream().map(userService::getById).toList());

        entity = repo.save(entity);
        evictTopicCache(null);
        return entity;
    }

    
    private void evictTopicCache(Integer topicId) {
        try {
            redisService.delete(RedisKey.managerGroupAll());
            if (topicId != null) {
                redisService.delete(RedisKey.managerGroupById(topicId));
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public void delete(Integer id) { evictTopicCache(id);
        repo.deleteById(id); }
}
