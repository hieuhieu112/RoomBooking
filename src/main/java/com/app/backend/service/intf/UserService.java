package com.app.backend.service.intf;

import java.util.List;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.entity.Booking;
import com.app.backend.entity.ManagerGroup;
import com.app.backend.entity.User;
import com.app.backend.entity.enumm.Status;
import org.springframework.data.domain.Page;

public interface UserService {
    UserResponse mapToResponse(User entity);
    User create(UserRequest request, Status status);
    User getById(Integer id);
    User getByEmail(String email);
    User getByUsername(String username);
    List<User> getAll();
    Page<User> getByPage(int page, int size);
    User update(Integer id, UserUpdateRequest request);
    void delete(Integer id);

    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
    public User updateMyAccount(UserUpdateRequest request);
    public void activeUser(String username);
    public User changePass(User u, String newPass);
    public User createSaveDB(UserRequest request, Status s);
    public User changeManageGroup(User user, ManagerGroup managerGroup);
}
