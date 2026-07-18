package com.app.backend.service.impl;

import com.app.backend.exception.CommonException;
import com.app.backend.exception.ErrorCode;
import com.app.backend.service.intf.FileStorageService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;


@Service
public class FileStorageServiceImpl implements FileStorageService {

    private static final Map<String, String> IMAGE_EXTENSIONS_BY_CONTENT_TYPE =
            Map.of(
                    "image/png", ".png",
                    "image/jpeg", ".jpg",
                    "image/webp", ".webp",
                    "image/gif", ".gif"
            );
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public String uploadFile(MultipartFile file) {
        try{
            if (file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            String originalFilename =
                    file.getOriginalFilename();

            String extension =
                    originalFilename.substring(
                            originalFilename.lastIndexOf(".")
                    );

            int dot = originalFilename.lastIndexOf('.');

            if (dot == -1) {
                throw new CommonException(ErrorCode.FILE_NOT_EXTENSION);
            }

            String fileName = UUID.randomUUID()+extension;

            Path uploadPath = Paths.get(uploadDir);

            Files.createDirectories(uploadPath);

            Path targetFile =
                    uploadPath.resolve(fileName);

            Files.copy(
                    file.getInputStream(),
                    targetFile,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return uploadDir + fileName;
        }
        catch (IOException e) {
            throw new CommonException(
                    ErrorCode.FILE_UPLOAD_FAILED
            );
        }
    }

    @Override
    public void downloadFile(String fileUUIDName, HttpServletResponse response) {
        Path uploadPath =Paths.get(uploadDir);

        Path path = uploadPath.resolve(fileUUIDName);

        try(InputStream in = Files.newInputStream(path)){
            response.setContentLengthLong(Files.size(path));

            OutputStream out =
                    response.getOutputStream();
            byte[] buffer =
                    new byte[8192];

            int len;

            while ((len = in.read(buffer)) != -1) {

                out.write(buffer, 0, len);

            }

            out.flush();

        } catch (IOException e) {
            throw new CommonException(
                    ErrorCode.FILE_NOT_FOUND
            );
        }
    }

    @Override
    public void delete(String fileUrl) {

    }

    @Override
    public void deleteRoomImage(Long examId) {

    }
}
