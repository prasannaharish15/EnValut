package com.myvalut.envalut_backend.controller;

import com.myvalut.envalut_backend.service.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;


@RestController
@RequestMapping("/api/file")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;

    }


    @PostMapping("/uploadfile")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, Authentication authentication) {
        try {
            return fileService.uploadEncryptedFile(authentication.getName(), file);


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", e.getMessage()));

        }
    }
    @GetMapping("/getallfile")
    public ResponseEntity<?> getAllDocuments(Authentication auth){
        return fileService.getAllDocuments(auth.getName());
    }

    @GetMapping("/viewordownload/{id}")
    public ResponseEntity<?>getViewORDownloadFile(@PathVariable("id") Long id,Authentication authentication){
        return fileService.getViewORDownloadFile(id,authentication.getName());
    }
    @DeleteMapping("/deletefile/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable("id") Long id,Authentication authentication) throws Exception {
        return fileService.deleteFile(id,authentication.getName());
    }
}
