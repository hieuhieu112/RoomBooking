//package com.app.backend.service.impl;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import com.app.backend.dtos.request.*;
//import com.app.backend.dtos.response.*;
//import com.app.backend.entity.RoomImage;
//import com.app.backend.repository.RoomImageRepository;
//import com.app.backend.service.intf.RoomImageService;
//
//@Service
//@Transactional
//@AllArgsConstructor
//public class RoomImageServiceImpl implements RoomImageService {
//    private final RoomImageRepository repo;
//
//    private RoomImageResponse mapToResponse(RoomImage entity) {
//        RoomImageResponse resp = new RoomImageResponse();
//        resp.setId(entity.getId());
//        resp.setUrl(entity.getUrl());
//        resp.setRoomId(entity.getRoomId());
//        return resp;
//    }
//
//    @Override
//    public RoomImageResponse create(RoomImageRequest request) {
//        RoomImage entity = new RoomImage();
//        entity.setUrl(request.getUrl());
//        entity.setRoomId(request.getRoomId());
//        entity = repo.save(entity);
//        return mapToResponse(entity);
//    }
//
//    @Override
//    public RoomImageResponse getById(Integer id) {
//        return repo.findById(id).map(this::mapToResponse).orElse(null);
//    }
//
//    @Override
//    public List<RoomImageResponse> getAll() {
//        return repo.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
//    }
//
//    @Override
//    public RoomImageResponse update(Integer id, RoomImageRequest request) {
//        RoomImage entity = repo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
//        entity.setUrl(request.getUrl());
//        entity.setRoomId(request.getRoomId());
//        entity = repo.save(entity);
//        return mapToResponse(entity);
//    }
//
//    @Override
//    public void delete(Integer id) { repo.deleteById(id); }
//}
