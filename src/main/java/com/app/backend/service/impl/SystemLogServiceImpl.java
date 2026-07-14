package com.app.backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.app.backend.exception.CommonException;
import com.app.backend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.entity.SystemLog;
import com.app.backend.repository.SystemLogRepository;
import com.app.backend.service.SystemLogService;

@Service
@Transactional
@RequiredArgsConstructor
public class SystemLogServiceImpl implements SystemLogService {
    private final SystemLogRepository repo;
    private final UserServiceImpl userService;

    public SystemLogResponse mapToResponse(SystemLog entity) {
        SystemLogResponse resp = new SystemLogResponse();
        resp.setId(entity.getId());
        resp.setUserId(entity.getUser().getId());
        resp.setAction(entity.getLogAction().name());
        resp.setTargetType(entity.getTargetType().name());
        resp.setTimestamp(entity.getTimestamp());
        resp.setIsDeleted(entity.getIsDelete());
        resp.setDescription(entity.getDescription());
        return resp;
    }

    @Override
    public SystemLog create(SystemLogRequest request) {
        SystemLog entity = new SystemLog();

        entity.setUser(userService.getById(request.getUserId()));
        entity.setLogAction(request.getAction());
        entity.setTargetType(request.getTargetType());
        entity.setTimestamp(request.getTimestamp());
        entity.setIsDelete(request.getIsDeleted());
        entity.setDescription(request.getDescription());
        entity = repo.save(entity);
        return entity;
    }

    @Override
    public SystemLog getById(Integer id) {

        return repo.findById(id).orElseThrow(() -> new CommonException(ErrorCode.LOG_NOT_FOUND));
    }

    @Override
    public List<SystemLog> getAll() {
        return repo.findAll();
    }

    @Override
    public SystemLog update(Integer id, SystemLogRequest request) {
        SystemLog entity = getById(id);
        entity.setUser(userService.getById(request.getUserId()));
        entity.setLogAction(request.getAction());
        entity.setTargetType(request.getTargetType());
        entity.setTimestamp(request.getTimestamp());
        entity.setIsDelete(request.getIsDeleted());
        entity.setDescription(request.getDescription());
        entity = repo.save(entity);
        return entity;
    }

    @Override
    public void delete(Integer id) { repo.deleteById(id); }
}
