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
import com.app.backend.entity.House;
import com.app.backend.repository.HouseRepository;
import com.app.backend.service.HouseService;

@Service
@Transactional
@AllArgsConstructor
public class HouseServiceImpl implements HouseService {
    private final HouseRepository repo;

    public HouseResponse mapToResponse(House entity) {
        HouseResponse resp = new HouseResponse();
        resp.setId(entity.getId());
        resp.setName(entity.getName());
        return resp;
    }

    @Override
    public House create(HouseRequest request) {
        House entity = new House();
        entity.setName(request.getName());
        entity = repo.save(entity);
        return entity;
    }

    @Override
    public House getById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new CommonException(ErrorCode.HOUSE_NOT_FOUND));
    }

    @Override
    public List<House> getAll() {
        return repo.findAll();
    }

    @Override
    public House update(Integer id, HouseRequest request) {
        House entity = repo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        entity.setName(request.getName());
        entity = repo.save(entity);
        return entity;
    }

    @Override
    public void delete(Integer id) { repo.deleteById(id); }
}
