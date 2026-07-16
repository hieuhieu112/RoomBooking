package com.app.backend.service.intf;

import java.util.List;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.entity.User;

public interface UserService {
    UserResponse mapToResponse(User entity);
    User create(UserRequest request);
    User getById(Integer id);
    User getByEmail(String email);
    User getByUsername(String username);
    List<User> getAll();
    User update(Integer id, UserUpdateRequest request);
    void delete(Integer id);
}
