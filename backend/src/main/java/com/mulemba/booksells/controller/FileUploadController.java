package com.mulemba.booksells.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    @Value("${app.upload.dir:uploads/}")
    private String uploadDir;

    @PostMapping
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Ficheiro vazio"));
        }

        try {
            File uploadDirectory = new File(uploadDir);
            if (!uploadDirectory.exists()) {
                uploadDirectory.mkdirs();
            }

            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            if (originalFilename == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Nome de ficheiro inválido"));
            }

            String lowerCaseName = originalFilename.toLowerCase();
            if (!lowerCaseName.endsWith(".png") && !lowerCaseName.endsWith(".jpg") && 
                !lowerCaseName.endsWith(".jpeg") && !lowerCaseName.endsWith(".pdf") && 
                !lowerCaseName.endsWith(".epub")) {
                return ResponseEntity.badRequest().body(Map.of("error", "Extensão não permitida. Apenas PNG, JPG, PDF e EPUB são suportados."));
            }

            String newFilename = UUID.randomUUID().toString() + fileExtension;
            
            // Format path correctly depending on trailing slash
            String basePath = uploadDir.endsWith("/") ? uploadDir : uploadDir + "/";
            Path path = Paths.get(basePath + newFilename);
            Files.write(path, file.getBytes());

            // Endpoint that serves the files maps /uploads/** -> basePath
            String fileUrl = "/uploads/" + newFilename;
            return ResponseEntity.ok(Map.of("url", fileUrl));

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro ao guardar ficheiro"));
        }
    }
}
