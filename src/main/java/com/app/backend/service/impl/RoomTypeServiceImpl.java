package com.app.backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.app.backend.exception.CommonException;
import com.app.backend.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.entity.RoomType;
import com.app.backend.repository.RoomTypeRepository;
import com.app.backend.service.RoomTypeService;

@Service
@Transactional
@AllArgsConstructor
public class RoomTypeServiceImpl implements RoomTypeService {
    private final RoomTypeRepository repo;

    public RoomTypeResponse mapToResponse(RoomType entity) {
        RoomTypeResponse resp = new RoomTypeResponse();
        resp.setId(entity.getId());
        resp.setName(entity.getName());
        return resp;
    }

    @Override
    public RoomType create(RoomTypeRequest request) {
        RoomType entity = new RoomType();
        entity.setName(request.getName());
        entity = repo.save(entity);
        return entity;
    }

    @Override
    public RoomType getById(Integer id) {

        return repo.findById(id).orElseThrow(() -> new CommonException(ErrorCode.ROOM_TYPE_NOT_FOUND));
    }

    @Override
    public List<RoomType> getAll() {
        return repo.findAll();
    }

    @Override
    public RoomType update(Integer id, RoomTypeRequest request) {
        RoomType entity = getById(id);
        entity.setName(request.getName());
        entity = repo.save(entity);
        return entity;
    }

    @Override
    public void delete(Integer id) { repo.deleteById(id); }
}
