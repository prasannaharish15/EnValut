package com.myvalut.envalut_backend.service;

import com.myvalut.envalut_backend.model.EncryptedFile;
import com.myvalut.envalut_backend.model.User;
import com.myvalut.envalut_backend.repository.EncryptedFileRepository;
import com.myvalut.envalut_backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class FileService {

    private final EncryptionService encryptionService;
    private final UserRepository userRepository;
    private final EncryptedFileRepository encryptedFileRepository;
    public FileService(EncryptionService encryptionService,UserRepository userRepository,EncryptedFileRepository encryptedFileRepository){
        this.encryptionService=encryptionService;
        this.userRepository=userRepository;
        this.encryptedFileRepository=encryptedFileRepository;
    }

    private final Path uploadDir= Paths.get("uploads");


    private void initUploaddir() throws IOException {
        if(!Files.exists(uploadDir)){
            Files.createDirectory(uploadDir);
        }
    }

    public ResponseEntity<?> uploadEncryptedFile(String email, MultipartFile file) throws Exception {
        String originalFilename =file.getOriginalFilename();
        if(originalFilename.isEmpty()){
            throw new IllegalArgumentException("File Name cannot be null");
        }
        byte[] fileBytes= file.getBytes();
        if(fileBytes.length == 0){
            throw new IllegalArgumentException("Empty File");
        }
        String ext= StringUtils.getFilenameExtension(originalFilename);
        if(ext != null && ext.equalsIgnoreCase("exe")){
            throw new IllegalArgumentException("Exe file not allowed");
        }

        initUploaddir();
        SecretKey aesKey=encryptionService.generateFileKey();
        byte[] encryptedBytes=encryptionService.encryptFile(fileBytes,aesKey);
        String storedName=System.currentTimeMillis()+"_"+originalFilename;

        Path filePath=uploadDir.resolve(storedName);
        Files.write(filePath,encryptedBytes);

        String encryptedAesKey=encryptionService.encrypAesKey(aesKey);

        Optional<User> user=userRepository.findByEmail(email);
        if(user.isEmpty()){
            throw new Exception("User not found");
        }


        EncryptedFile encFile=new EncryptedFile();
        encFile.setUser_id(user.get().getId());
        encFile.setFile_name(originalFilename);
        encFile.setStored_name(storedName);
        encFile.setFile_path(String.valueOf(filePath));
        encFile.setFile_type(ext);
        encFile.setFile_size(String.valueOf(file.getSize()/(1024*1024)));
        encFile.setEncryption_key(encryptedAesKey);
        encFile.setCreated_at(LocalDateTime.now());

        encFile.setCategory_id(null);
        encryptedFileRepository.save(encFile);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message","file uploaded successfully"));





    }
}
