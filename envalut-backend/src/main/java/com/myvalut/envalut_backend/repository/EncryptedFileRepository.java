package com.myvalut.envalut_backend.repository;

import com.myvalut.envalut_backend.model.EncryptedFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EncryptedFileRepository extends JpaRepository<EncryptedFile,Long> {
}
