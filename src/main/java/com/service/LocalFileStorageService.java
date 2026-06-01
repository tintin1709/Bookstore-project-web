package com.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class LocalFileStorageService implements FileStorageService {

    private final String uploadDir = "uploads/";

    @Override
    public String store(MultipartFile file) {
        // Validate file
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("No file selected");
        }

        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null || originalFilename.isBlank()) {
            throw new IllegalArgumentException("Invalid file name");
        }

        if (!originalFilename.toLowerCase().matches(".*\\.(png|jpg|jpeg|webp)$")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }
        
        // Generate a unique filename to prevent overwriting
        try {
            String filename = System.currentTimeMillis() + "_"
                    + StringUtils.cleanPath(file.getOriginalFilename());

            Path path = Paths.get(uploadDir);
            Files.createDirectories(path);

            Path filePath = path.resolve(filename);
            file.transferTo(filePath.toFile());

            return "/uploads/" + filename;

        } catch (Exception e) {
            throw new RuntimeException("File upload failed", e);
        }
    }
}