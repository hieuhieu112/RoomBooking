package com.app.backend.service.impl;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import com.app.backend.constant.RedisKey;
import com.app.backend.exception.CommonException;
import com.app.backend.exception.ErrorCode;
import com.app.backend.service.CacheService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.entity.Role;
import com.app.backend.repository.RoleRepository;
import com.app.backend.service.intf.RoleService;

@Service
@Transactional
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository repo;
    private final CacheService cacheService;

    private static final Duration ROLE_CACHE_TTL = Duration.ofMinutes(15);

    public RoleResponse mapToResponse(Role entity) {
        RoleResponse resp = new RoleResponse();
        resp.setId(entity.getId());
        resp.setName(entity.getName());
        resp.setIsAdmin(entity.getIsAdmin());
        resp.setIsManager(entity.getIsManager());
        return resp;
    }

    private Role convertFromRequest(Role entity, RoleRequest request){
        entity.setName(request.getName());
        entity.setIsAdmin(request.getIsAdmin());
        entity.setIsManager(request.getIsManager());
        return  entity;
    }

    @Override
    public Role create(RoleRequest request) {
        Role entity = repo.save(convertFromRequest(new Role(), request));
        evictTopicCache(null);
        return (entity);
    }

    @Override
    public Role getById(Integer id) {
        return
                cacheService.getOrLoad(
                        RedisKey.roleById(id),
                        Role.class,
                        ROLE_CACHE_TTL,
                        () -> repo.findById(id).orElseThrow(() -> new CommonException(ErrorCode.ROLE_NOT_FOUND))
                );
    }

    @Override
    public Role getByName(String name) {
        return
                cacheService.getOrLoad(
                        RedisKey.roleByName(name),
                        Role.class,
                        ROLE_CACHE_TTL,
                        () -> repo.findByName(name).orElseThrow(() -> new CommonException(ErrorCode.ROLE_NOT_FOUND))
                );
    }

    @Override
    public List<Role> getAll() {

        Role[] roles =      cacheService.getOrLoad(
                        RedisKey.roleAll(),
                        Role[].class,
                        ROLE_CACHE_TTL,
                        () -> repo.findAll()
                                .toArray(new Role[0])
                );

        return Arrays.asList(roles);
    }

    @Override
    public Role update(Integer id, RoleRequest request) {
        Role entity = repo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));

        entity = repo.save(convertFromRequest(entity, request));
        evictTopicCache(entity.getId());
        evictTopicCacheName(entity.getName());
        return (entity);
    }

    private void evictTopicCache(Integer topicId) {

        try {
            cacheService.evict(RedisKey.roleAll());

            if (topicId != null) {
                cacheService.evict(RedisKey.roleById(topicId));
            }
        } catch (Exception ignored) {
        }
    }

    private void evictTopicCacheName(String topicName) {

        try {
            cacheService.evict(RedisKey.roleAll());

            if (topicName != null) {
                cacheService.evict(RedisKey.roleByName(topicName));
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public void delete(Integer id) {
        Role r =getById(id);
        evictTopicCache(id);
        evictTopicCacheName(r.getName());
        repo.deleteById(id); }
}
