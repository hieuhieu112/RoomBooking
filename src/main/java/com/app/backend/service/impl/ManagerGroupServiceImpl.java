package com.app.backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.entity.ManagerGroup;
import com.app.backend.repository.ManagerGroupRepository;
import com.app.backend.service.ManagerGroupService;

@Service
@Transactional
@RequiredArgsConstructor
public class ManagerGroupServiceImpl implements ManagerGroupService {
    private final ManagerGroupRepository repo;

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
        entity = repo.save(entity);
        return entity;
    }

    @Override
    public ManagerGroup getById(Integer id) {
        return repo.findById(id).orElseThrow();//repo.findById(id).map(this::mapToResponse).orElse(null);
    }

    @Override
    public List<ManagerGroup> getAll() {
        return repo.findAll();
    }

    @Override
    public ManagerGroup update(Integer id, ManagerGroupRequest request) {
        ManagerGroup entity = repo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        entity.setName(request.getName());
        entity = repo.save(entity);
        return entity;
    }

    @Override
    public void delete(Integer id) { repo.deleteById(id); }
}
