package com.app.backend.service;

import java.util.List;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.entity.ManagerGroup;

public interface ManagerGroupService {
    ManagerGroupResponse mapToResponse(ManagerGroup entity);
    ManagerGroup create(ManagerGroupRequest request);
    ManagerGroup getById(Integer id);
    List<ManagerGroup> getAll();
    ManagerGroup update(Integer id, ManagerGroupRequest request);
    void delete(Integer id);
}
