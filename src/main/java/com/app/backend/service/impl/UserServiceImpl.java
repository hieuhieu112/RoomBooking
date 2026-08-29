package com.app.backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

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
    private final RoleServiceImpl roleService;
//    private final ManagerGroupServiceImpl managerGroupService;
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
        return createSaveDB(request, null);
    }

    public User createSaveDB(UserRequest request, Status status){
        if(existsByEmail(request.getEmail())){
            throw  new CommonException(ErrorCode.USER_EMAIL_EXISTS);
        }

        if (existsByUsername(request.getUsername())){
            throw new CommonException(ErrorCode.USER_USERNAME_EXISTS);
        }

        User entity = new User();
        entity.setName(request.getName());
        entity.setEmail(request.getEmail());
        entity.setIncidentCount(request.getIncidentCount());
        entity.setPassword(passwordEncoder.encode(request.getPass()));
        entity.setStatus(
                status != null ? status : request.getStatus()
        );
        entity.setUsername(request.getUsername());
//        ManagerGroup managerGroup =  managerGroupService.getById(request.getManagerGroupId());

//        entity.setManagerGroup(managerGroup);

        entity.setRoles(request.getRoles().stream().map(roleService::getByName).collect(Collectors.toSet()));
        entity = save(entity);
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
    public User update(Integer id, UserUpdateRequest request) {

        User entity = getById(id);
        entity.setName(request.getName());
        if(!entity.getEmail().equals(request.getEmail())){
            entity.setEmail(request.getEmail());
        }

//        entity.setIncidentCount(request.getIncidentCount());
//        entity.setPassword(request.getPass());
//        entity.setUsername(request.getUsername());
//        ManagerGroup managerGroup = managerGroupService.getById(request.getManagerGroupId());
//        entity.setManagerGroup(managerGroup);
        entity = save(entity);
        return entity;
    }
    public void activeUser(String username){
        User user = getByUsername(username);

        user.setStatus(Status.ACTIVE);

        save(user);
    }

    public User changeManageGroup(User user, ManagerGroup managerGroup){
        user.setManagerGroup(managerGroup);
        return repo.save(user);
    }

    private User save(User user){
        return repo.save(user);
    }

    @Override
    public void delete(Integer id) { repo.deleteById(id); }

    @Override
    public Boolean existsByEmail(String email) {
        return repo.existsByEmail(email);
    }

    @Override
    public Boolean existsByUsername(String username) {
        return repo.existsByUsername(username);
    }
}
