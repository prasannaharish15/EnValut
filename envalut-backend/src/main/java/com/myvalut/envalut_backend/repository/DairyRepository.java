package com.myvalut.envalut_backend.repository;

import com.myvalut.envalut_backend.model.Dairy;
import com.myvalut.envalut_backend.model.EncryptedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DairyRepository extends JpaRepository<Dairy,Long> {
    List<Dairy> findAllByUserId(Long userId);
}
