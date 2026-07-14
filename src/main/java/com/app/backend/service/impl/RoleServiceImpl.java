package com.app.backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.app.backend.exception.CommonException;
import com.app.backend.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.entity.Role;
import com.app.backend.repository.RoleRepository;
import com.app.backend.service.RoleService;

@Service
@Transactional
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository repo;

    public RoleResponse mapToResponse(Role entity) {
        RoleResponse resp = new RoleResponse();
        resp.setId(entity.getId());
        resp.setName(entity.getName());
        return resp;
    }

    @Override
    public Role create(RoleRequest request) {
        Role entity = new Role();
        entity.setName(request.getName());
        entity = repo.save(entity);
        return (entity);
    }

    @Override
    public Role getById(Integer id) {

        return repo.findById(id).orElseThrow(() -> new CommonException(ErrorCode.ROLE_NOT_FOUND));
    }

    @Override
    public List<Role> getAll() {
        return repo.findAll();
    }

    @Override
    public Role update(Integer id, RoleRequest request) {
        Role entity = repo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        entity.setName(request.getName());
        entity = repo.save(entity);
        return (entity);
    }

    @Override
    public void delete(Integer id) { repo.deleteById(id); }
}
