package com.app.backend.service;

import java.util.List;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;

public interface RoomImageService {
    RoomImageResponse create(RoomImageRequest request);
    RoomImageResponse getById(Integer id);
    List<RoomImageResponse> getAll();
    RoomImageResponse update(Integer id, RoomImageRequest request);
    void delete(Integer id);
}
