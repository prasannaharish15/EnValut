package com.myvalut.envalut_backend.service;

import com.myvalut.envalut_backend.model.EncryptedFile;
import com.myvalut.envalut_backend.model.User;
import com.myvalut.envalut_backend.repository.EncryptedFileRepository;
import com.myvalut.envalut_backend.repository.UserRepository;
import lombok.NonNull;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
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
import java.util.List;
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

    public  ResponseEntity<?> getAllDocuments(String email) {
        Optional<User> user=userRepository.findByEmail(email);
        if(user.isEmpty()){
          return  ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("Message","User Not found"));

        }
        User currUser=user.get();
        List<EncryptedFile> allFile=encryptedFileRepository.findAllByUserId(currUser.getId());
        int i=0;
        int j=allFile.size()-1;
        while(i<j){
            EncryptedFile file=allFile.get(i);
            allFile.set(i,allFile.get(j));
            allFile.set(j,file);
            i++;
            j--;
        }
        System.out.println(allFile);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(allFile);

    }


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
        encFile.setUserId(user.get().getId());
        encFile.setFile_name(originalFilename);
        encFile.setStored_name(storedName);
        encFile.setFile_path(storedName);
        encFile.setFile_type(ext);
        encFile.setFile_size(String.valueOf(file.getSize()/(1024*1024)));
        encFile.setEncryption_key(encryptedAesKey);
        encFile.setCreated_at(LocalDateTime.now());

        encFile.setCategory_id(null);
        encryptedFileRepository.save(encFile);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message","file uploaded successfully"));





    }

    public ResponseEntity<?> getViewORDownloadFile(Long id, String email) {
        try {
            Optional<User> user=userRepository.findByEmail(email);
            if(user.isEmpty()){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message","user not found"));
            }
            User currUser=user.get();
            Optional<EncryptedFile> encryptedFile=encryptedFileRepository.findById(id);
            if(encryptedFile.isEmpty()){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message","file not found"));

            }
            EncryptedFile currFile=encryptedFile.get();
            if(!currFile.getUserId().equals(currUser.getId())){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message","Access denied"));
            }
            byte[] encryptedBytes=getStoredFileBytes(currFile.getFile_path());

            //decrypt AES Key and File
            SecretKey aesKey=encryptionService.decryptAesKey(currFile.getEncryption_key());

            byte[] decryptedFile=encryptionService.decryptFile(encryptedBytes,aesKey);

            ByteArrayResource resource=new ByteArrayResource(decryptedFile);
            String contentType = Files.probeContentType(Paths.get(currFile.getFile_path()));
            if (contentType == null && currFile.getFile_type() != null) {
                contentType = switch (currFile.getFile_type().toLowerCase()) {
                    case "png" -> "image/png";
                    case "jpg", "jpeg" -> "image/jpeg";
                    case "pdf" -> "application/pdf";
                    case "txt" -> "text/plain";
                    case "doc" -> "application/msword";
                    case "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

                    default -> "application/octet-stream";
                };
            }
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + currFile.getFile_name() + "\"");
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(decryptedFile.length));
            if (contentType != null) headers.add(HttpHeaders.CONTENT_TYPE, contentType);

            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(resource);


        } catch (Exception e) {
            System.out.println("ERROR OCCURRED: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("messag", e.getMessage()));
        }
    }

    private byte[] getStoredFileBytes(String filePath) throws IOException {
        Path fullPath = uploadDir.resolve(filePath);
        System.out.println("Looking for file at: " + fullPath.toAbsolutePath());
        return Files.readAllBytes(fullPath);
    }

    public ResponseEntity<?> deleteFile(Long id, String email) throws IOException {
        Optional<User> user=userRepository.findByEmail(email);
        if(user.isEmpty()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message","user not found"));
        }
        Optional<EncryptedFile> encryptedFile=encryptedFileRepository.findById(id);
        if(encryptedFile.isEmpty()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message","file not found"));

        }
        User currUser=user.get();
        EncryptedFile currFile=encryptedFile.get();
        if(!currFile.getUserId().equals(currUser.getId())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message","Access denied"));
        }
        String fileName=currFile.getStored_name();
        Files.delete(uploadDir.resolve(fileName));
        encryptedFileRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message","File deleted successfully"));
    }
}
