package com.app.backend.service.intf;

import java.util.List;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.entity.Room;
import org.springframework.web.multipart.MultipartFile;

public interface RoomService {
    RoomResponse mapToResponse(Room entity);
    Room create(RoomRequest request, List<MultipartFile> images);
    Room getById(Integer id);
    List<Room> getAll();
    Room update(Integer id, RoomRequest request);
    void delete(Integer id);
}
