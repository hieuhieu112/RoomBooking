package com.app.backend.service.intf;

import java.util.List;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.entity.Room;
import com.app.backend.entity.RoomImage;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface RoomImageService {
    void getImageByID(Integer id, HttpServletResponse response);
    void deleteByRoomId(Integer id);
    RoomImage generateImage(MultipartFile image, Room room, Integer numberDisplay);
    RoomImageResponse mapToResponse(RoomImage entity);
    RoomImage create(RoomImageRequest request, Room room);
    RoomImage getById(Integer id);
    List<RoomImage> getByRoomId(Integer id);
    RoomImage update(Integer id, RoomImageRequest request, Room room);
    void delete(Integer id);
}
