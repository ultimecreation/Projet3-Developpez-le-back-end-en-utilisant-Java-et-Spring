package com.chatop.backend.service;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService {
    @Value("${site.url}")
    String siteUrl;
    private String filePathToSaveInDb = "";
    private String filename = "";
    private String uploadPath = "backend/src/main/resources/static/uploads/";

    /**
     * @param file      incoming file
     * @param subFolder user id represents the subfolder
     * @return returns the file url
     */
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

            this.filePathToSaveInDb = siteUrl + "/uploads/" + subFolder + "/" + this.filename;
            return filePathToSaveInDb;
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * @param file generate the filename
     * @return return the filename as string
     */
    public String getFilename(MultipartFile file) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        var timeInMillis = timestamp.getTime();
        return this.filename = timeInMillis + "_" + file.getOriginalFilename();
    }
}