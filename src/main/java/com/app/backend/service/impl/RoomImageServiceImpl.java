package com.app.backend.service.impl;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import com.app.backend.entity.Room;
import com.app.backend.exception.CommonException;
import com.app.backend.exception.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.entity.RoomImage;
import com.app.backend.repository.RoomImageRepository;
import com.app.backend.service.intf.RoomImageService;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@AllArgsConstructor
public class RoomImageServiceImpl implements RoomImageService {
    private final RoomImageRepository repo;
    private final FileStorageServiceImpl fileStorageService;


    @Override
    public RoomImage generateImage(MultipartFile image, Room room, Integer numberDisplay) {
        RoomImage entity = new RoomImage();

        entity.setRoom(room);
        String url = fileStorageService.uploadFile(image);
        entity.setUrl(url);
        entity.setStoredName(url.substring(url.lastIndexOf("/") + 1));
        entity.setOriginalName(image.getOriginalFilename());
        entity.setDisplayOrder(numberDisplay);
        return entity;
    }

    @Override
    public RoomImageResponse mapToResponse(RoomImage entity) {
        RoomImageResponse resp = new RoomImageResponse();
        resp.setId(entity.getId());
        resp.setUrl("/api/v1/roomimages/" + entity.getId());
        resp.setRoomId(entity.getRoom().getId());
        resp.setDisplayOrder(entity.getDisplayOrder());
        resp.setOriginalName(entity.getOriginalName());
        return resp;
    }

    @Override
    public RoomImage create(RoomImageRequest request, Room room) {
        RoomImage entity = new RoomImage();
        entity.setUrl(request.getUrl());
        entity.setRoom(room);
        entity = repo.save(entity);
        return (entity);
    }

    @Override
    public RoomImage getById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new CommonException(ErrorCode.ROOM_NOT_FOUND));
    }

    @Override
    public List<RoomImage> getByRoomId(Integer id) {
        return repo.findByRoomId(id);
    }

    @Override
    public void getImageByID(Integer id, HttpServletResponse response) {
        RoomImage roomImage = getById(id);
        String encoded = URLEncoder.encode(roomImage.getOriginalName(), StandardCharsets.UTF_8)
                .replace("+", "%20");

        response.setHeader(
                "Content-Disposition",
                "inline; filename=\"" + encoded  + "\""
        );

        fileStorageService.downloadFile(roomImage.getStoredName(), response);
    }

    @Override
    public void deleteByRoomId(Integer id) {
        repo.deleteByRoomId(id);
    }

    @Override
    public RoomImage update(Integer id, RoomImageRequest request, Room room) {
        RoomImage entity = repo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        entity.setUrl(request.getUrl());
        entity.setRoom(room);
        entity = repo.save(entity);
        return (entity);
    }

    @Override
    public void delete(Integer id) { repo.deleteById(id); }
}
