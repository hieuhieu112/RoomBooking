package com.app.backend.service.intf;

import java.util.List;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface RoomService {
    RoomResponse mapToResponse(Room entity);
    Room create(RoomRequest request, List<MultipartFile> images);
    Room getById(Integer id);
    Page<Room> getAllPagin(int page, int size);
    Page<Room> getAllByFilter(String search, Integer houseId,Integer roomTypeId, int page, int size);
    Room update(Integer id, RoomRequest request, List<MultipartFile> images);
    void delete(Integer id);
}
