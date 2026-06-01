package com.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class LocalFileStorageService implements FileStorageService {

    private final Path uploadPath = Paths.get(System.getProperty("user.dir"), "uploads");

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

        String cleanFilename = StringUtils.cleanPath(originalFilename);
        String lowerFilename = cleanFilename.toLowerCase();

        if (!lowerFilename.endsWith(".png")
                && !lowerFilename.endsWith(".jpg")
                && !lowerFilename.endsWith(".jpeg")
                && !lowerFilename.endsWith(".webp")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }

        try {
            // Create uploads folder if it does not exist
            Files.createDirectories(uploadPath);

            // Generate unique filename
            String filename = System.currentTimeMillis() + "_" + cleanFilename;

            Path targetPath = uploadPath.resolve(filename).normalize();

            // Prevent path traversal attack
            if (!targetPath.startsWith(uploadPath)) {
                throw new IllegalArgumentException("Invalid file path");
            }

            // Save file
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }

            return "/uploads/" + filename;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("File upload failed: " + e.getMessage(), e);
        }
    }
}