package com.app.backend.service.intf;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String uploadFile(MultipartFile file);

    void downloadFileDocument(String url, HttpServletResponse response);

    void delete(String fileUrl);

    void deleteRoomImage(Long examId);

}
