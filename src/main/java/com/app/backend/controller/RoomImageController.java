//package com.app.backend.controller;
//
//import java.util.List;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import lombok.AllArgsConstructor;
//import com.app.backend.dtos.request.*;
//import com.app.backend.dtos.response.*;
//import com.app.backend.service.intf.RoomImageService;
//
//@RestController
//@RequestMapping("/api/v1/roomimages")
//@AllArgsConstructor
//public class RoomImageController {
//    private final RoomImageService service;
//
//    @PostMapping
//    public ResponseEntity<DataResponse<RoomImageResponse>> create(@RequestBody RoomImageRequest request) {
//        DataResponse<RoomImageResponse> response = DataResponse.<RoomImageResponse>builder()
//                .data(service.mapToResponse(service.create(request)))
//                .statusCode(StatusRes.SUCCESS)
//                .message("SUCCESS")
//                .build();
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<DataResponse<RoomImageResponse>> getById(@PathVariable Integer id) {
//        var res = service.getById(id);
//        if (res != null) {
//            DataResponse<RoomImageResponse> response = DataResponse.<RoomImageResponse>builder()
//                    .data(service.mapToResponse(res))
//                    .statusCode(StatusRes.SUCCESS)
//                    .message("SUCCESS")
//                    .build();
//            return ResponseEntity.ok(response);
//        }
//        return ResponseEntity.notFound().build();
//    }
//
//    @GetMapping
//    public ResponseEntity<DataResponse<List<RoomImageResponse>>> getAll() {
//        DataResponse<List<RoomImageResponse>> response = DataResponse.<List<RoomImageResponse>>builder()
//                .data(service.getAll().stream().map(service::mapToResponse).toList())
//                .statusCode(StatusRes.SUCCESS)
//                .message("SUCCESS")
//                .build();
//        return ResponseEntity.ok(response);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<DataResponse<RoomImageResponse>> update(@PathVariable Integer id, @RequestBody RoomImageRequest request) {
//        DataResponse<RoomImageResponse> response = DataResponse.<RoomImageResponse>builder()
//                .data(service.mapToResponse(service.update(id, request)))
//                .statusCode(StatusRes.SUCCESS)
//                .message("SUCCESS")
//                .build();
//        return ResponseEntity.ok(response);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<DataResponse<Void>> delete(@PathVariable Integer id) {
//        service.delete(id);
//        DataResponse<Void> response = DataResponse.<Void>builder()
//                .statusCode(StatusRes.SUCCESS)
//                .message("SUCCESS")
//                .build();
//        return ResponseEntity.ok(response);
//    }
//}
