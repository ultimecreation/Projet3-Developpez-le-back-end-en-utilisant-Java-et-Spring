package com.chatop.backend.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.chatop.backend.entity.User;

@Service
public class FileUploadService {

    private String filePathToSaveInDb = "";
    private String filename = "";
    private String uploadPath = "backend/src/main/resources/static/uploads/";

    public String uploadFile(MultipartFile file, Integer subFolder) {
        Path root = Paths.get(uploadPath.concat(subFolder.toString()));

        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }

        try {
            this.filename = this.getFilename(file);
            Files.copy(file.getInputStream(), root.resolve(this.filename));

            this.filePathToSaveInDb = "uploads/" + subFolder + "/" + this.filename;
            return filePathToSaveInDb;
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }

            throw new RuntimeException(e.getMessage());
        }

    }

    public String getFilename(MultipartFile file) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        var timeInMillis = timestamp.getTime();
        return this.filename = timeInMillis + "_" + file.getOriginalFilename();
    }

    // public boolean fileExists(String finalPathPart) {

    // Path path = Paths.get(uploadPath + finalPathPart);
    // if (Files.exists(path)) {
    // return true;
    // }
    // return false;
    // }
}
// 1732391719299_html_validation.PNG