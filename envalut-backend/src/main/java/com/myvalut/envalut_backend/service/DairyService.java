package com.myvalut.envalut_backend.service;

import com.myvalut.envalut_backend.dto.DairyRequestDTO;
import com.myvalut.envalut_backend.dto.DairyResponseDTO;
import com.myvalut.envalut_backend.model.Dairy;
import com.myvalut.envalut_backend.model.EncryptedFile;
import com.myvalut.envalut_backend.model.User;
import com.myvalut.envalut_backend.repository.DairyRepository;
import com.myvalut.envalut_backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DairyService {
    private final UserRepository userRepository;
    private final DairyRepository dairyRepository;
    private final EncryptionService encryptionService;
    public DairyService(UserRepository userRepository, DairyRepository dairyRepository,EncryptionService encryptionService){
        this.userRepository=userRepository;
        this.dairyRepository=dairyRepository;
        this.encryptionService=encryptionService;
    }
    public ResponseEntity<?> saveDairyToDb(DairyRequestDTO dairyRequestDTO, Authentication authentication) throws Exception {
        Optional<User> user=userRepository.findByEmail(authentication.getName());
        if(user.isEmpty()){
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message","user not found"));
        }
        SecretKey aesKey=encryptionService.generateFileKey();

        byte[] encrypted_title=encryptionService.encryptFile(dairyRequestDTO.getTitle().getBytes(StandardCharsets.UTF_8),aesKey);
        byte[] encrypted_content=encryptionService.encryptFile(dairyRequestDTO.getText().getBytes(StandardCharsets.UTF_8),aesKey);
        String encrypted_aesKey=encryptionService.encrypAesKey(aesKey);

        Dairy entity=new Dairy();
        entity.setUserId(user.get().getId());
        entity.setEncryptedTitle(encrypted_title);
        entity.setEncryptedText(encrypted_content);
        entity.setEncryptedAesKey(encrypted_aesKey);
        entity.setCreatedOn(LocalDateTime.now());
        dairyRepository.save(entity);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message","content saved in db"));

    }

    public ResponseEntity<?> getDairy(Authentication email) throws Exception {
        Optional<User> user=userRepository.findByEmail(email.getName());
        if(user.isEmpty()){
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message","user not found"));
        }
        List<Dairy> entries=dairyRepository.findAllByUserId(user.get().getId());
        List<DairyResponseDTO>result=new ArrayList<>();
        for(Dairy entry:entries){
            SecretKey aesKey=encryptionService.decryptAesKey(entry.getEncryptedAesKey());
            String title=new String(encryptionService.decryptFile(entry.getEncryptedTitle(),aesKey));
            String content=new String(encryptionService.decryptFile(entry.getEncryptedText(),aesKey));

            DairyResponseDTO dairyResponseDTO=new DairyResponseDTO(entry.getId(),title,content,entry.getCreatedOn());
            result.add(dairyResponseDTO);
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(result);
    }
}
