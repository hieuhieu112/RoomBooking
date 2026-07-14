package com.app.backend.service.intf;

import java.util.List;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.entity.House;

public interface HouseService {
    HouseResponse mapToResponse(House entity);
    House create(HouseRequest request);
    House getById(Integer id);
    List<House> getAll();
    House update(Integer id, HouseRequest request);
    void delete(Integer id);
}
