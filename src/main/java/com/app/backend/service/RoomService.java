package com.app.backend.service;

import java.util.List;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.entity.Room;

public interface RoomService {
    RoomResponse mapToResponse(Room entity);
    Room create(RoomRequest request);
    Room getById(Integer id);
    List<Room> getAll();
    Room update(Integer id, RoomRequest request);
    void delete(Integer id);
}
