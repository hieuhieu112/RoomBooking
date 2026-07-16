package com.app.backend.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.app.backend.entity.RoomImage;
import com.app.backend.exception.CommonException;
import com.app.backend.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.entity.Room;
import com.app.backend.repository.RoomRepository;
import com.app.backend.service.intf.RoomService;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@AllArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository repo;
    private final RoomTypeServiceImpl roomTypeService;
    private final HouseServiceImpl houseService;
//    private final ManagerGroupServiceImpl managerGroupService;
    private final RoomImageServiceImpl roomImageService;

    public RoomResponse mapToResponse(Room entity) {
        RoomResponse resp = new RoomResponse();
        resp.setId(entity.getId());
        resp.setName(entity.getName());
        resp.setLocation(entity.getLocation());
        resp.setCapacity(entity.getCapacity());
        resp.setRoomTypeId(entity.getRoomType().getId());
        resp.setHouseId(entity.getHouse().getId());
        resp.setImage(entity.getImages().stream().map(
                RoomImage::getUrl
        ).toList());
        if(entity.getManagerGroup() != null){
            resp.setManagerGroupId(entity.getManagerGroup().getId());
        }

        return resp;
    }

    @Override
    public Room create(RoomRequest request, List<MultipartFile> images) {
        Room entity = new Room();
        entity.setName(request.getName());
        entity.setLocation(request.getLocation());
        entity.setCapacity(request.getCapacity());

        entity.setRoomType(roomTypeService.getById(request.getRoomTypeId()));
        entity.setHouse(houseService.getById(request.getHouseId()));
        //entity.setImage(new RoomImage() request.getImage());
//        entity.setManagerGroup(managerGroupService.getById(request.getManagerGroupId()));

        List<RoomImage> roomImages = new ArrayList<>();
        for(int i = 0; i< images.size(); i++){
            roomImages.add(roomImageService.generateImage(images.get(i), entity, i));
        }

        entity.setImages(roomImages);

        entity = repo.save(entity);
        return entity;
    }

    @Override
    public Room getById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new CommonException(ErrorCode.ROOM_NOT_FOUND));
    }

    @Override
    public List<Room> getAll() {
        return repo.findAll();
    }

    @Override
    public Room update(Integer id, RoomRequest request) {
        Room entity = getById(id);
        entity.setName(request.getName());
        entity.setLocation(request.getLocation());
        entity.setCapacity(request.getCapacity());
        entity.setRoomType(roomTypeService.getById(request.getRoomTypeId()));
        entity.setHouse(houseService.getById(request.getHouseId()));
        //entity.setImage(new RoomImage() request.getImage());
//        entity.setManagerGroup(managerGroupService.getById(request.getManagerGroupId()));
        entity = repo.save(entity);
        return (entity);
    }

    @Override
    public void delete(Integer id) { repo.deleteById(id); }
}
