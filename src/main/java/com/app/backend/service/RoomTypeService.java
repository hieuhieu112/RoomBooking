package com.app.backend.service;

import java.util.List;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.entity.RoomType;

public interface RoomTypeService {
    RoomTypeResponse mapToResponse(RoomType entity);
    RoomType create(RoomTypeRequest request);
    RoomType getById(Integer id);
    List<RoomType> getAll();
    RoomType update(Integer id, RoomTypeRequest request);
    void delete(Integer id);
}
