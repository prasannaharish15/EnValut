package com.myvalut.envalut_backend.repository;

import com.myvalut.envalut_backend.model.EncryptedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EncryptedFileRepository extends JpaRepository<EncryptedFile,Long> {

    List<EncryptedFile> findAllByUserId(Long userId);

    Optional<EncryptedFile> findById(Long id);


}
