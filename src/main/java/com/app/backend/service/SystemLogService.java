package com.app.backend.service;

import java.util.List;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.entity.SystemLog;

public interface SystemLogService {
    SystemLog create(SystemLogRequest request);
    SystemLog getById(Integer id);
    List<SystemLog> getAll();
    SystemLog update(Integer id, SystemLogRequest request);
    void delete(Integer id);
}
