package com.chatop.backend.service;

import java.io.IOException;
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
    private String siteUrl;
    private String filePathToSaveInDb = "";
    private String filename = "";
    private String uploadPath = "backend/src/main/resources/static/uploads/";

    /**
     * @param file      incoming file
     * @param subFolder user id represents the subfolder
     * @return returns the file url
     */
    public String uploadFile(MultipartFile file, Integer subFolder) throws IOException {
        Path root = Paths.get(uploadPath.concat(subFolder.toString()));

        Files.createDirectories(root);

        this.filename = this.getFilename(file);
        Files.copy(file.getInputStream(), root.resolve(this.filename));

        this.filePathToSaveInDb = siteUrl + "/uploads/" + subFolder + "/" + this.filename;
        return filePathToSaveInDb;

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