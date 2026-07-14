package com.app.backend.service.impl;

import java.util.List;

import com.app.backend.entity.ManagerGroup;
import com.app.backend.entity.Role;
import com.app.backend.entity.enumm.Status;
import com.app.backend.exception.CommonException;
import com.app.backend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.entity.User;
import com.app.backend.repository.UserRepository;
import com.app.backend.service.intf.UserService;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repo;
    private final ManagerGroupServiceImpl managerGroupService;
    private final PasswordEncoder passwordEncoder;

    public UserResponse mapToResponse(User entity) {
        UserResponse resp = new UserResponse();
        resp.setId(entity.getId());
        resp.setName(entity.getName());
        resp.setEmail(entity.getEmail());
        resp.setRoles(entity.getRoles().stream().map(Role::getName).toList());
        resp.setIncidentCount(entity.getIncidentCount());
//        resp.setPass(entity.getPassword());
        resp.setStatus(entity.getStatus().name());
        resp.setUsername(entity.getUsername());
//        resp.setManagerGroupId(entity.getManagerGroup().getId());
        return resp;
    }

    @Override
    public User create(UserRequest request) {
        User entity = new User();
        entity.setName(request.getName());
        entity.setEmail(request.getEmail());
        entity.setIncidentCount(request.getIncidentCount());
        entity.setPassword(passwordEncoder.encode(request.getPass()));
        entity.setStatus(Status.LOCKED);
        entity.setUsername(request.getUsername());
        ManagerGroup managerGroup =  managerGroupService.getById(request.getManagerGroupId());

        entity.setManagerGroup(managerGroup);
        entity = repo.save(entity);
        return entity;
    }

    @Override
    public User getById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public User getByEmail(String email) {
        return repo.findByEmail(email).orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public User getByUsername(String username) {
        return repo.findByUsername(username).orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND));
    }


    @Override
    public List<User> getAll() {
        return repo.findAll();
    }

    @Override
    public User update(Integer id, UserRequest request) {
        User entity = getById(id);
        entity.setName(request.getName());
        entity.setEmail(request.getEmail());
        entity.setIncidentCount(request.getIncidentCount());
//        entity.setPassword(request.getPass());
        entity.setUsername(request.getUsername());
        ManagerGroup managerGroup = managerGroupService.getById(request.getManagerGroupId());
        entity.setManagerGroup(managerGroup);
        entity = repo.save(entity);
        return entity;
    }

    @Override
    public void delete(Integer id) { repo.deleteById(id); }
}
