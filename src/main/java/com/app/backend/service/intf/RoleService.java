package com.app.backend.service.intf;

import java.util.List;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.entity.Role;

public interface RoleService {
    RoleResponse mapToResponse(Role entity);
    Role create(RoleRequest request);
    Role getById(Integer id);
    List<Role> getAll();
    Role update(Integer id, RoleRequest request);
    void delete(Integer id);
}
