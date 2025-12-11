package com.myvalut.envalut_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EncryptedFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;


    private Long category_id;

    @NonNull
    private String file_name;

    @NonNull
    private String stored_name;

    @NonNull
    private String file_path;


    private String file_type;

    @NonNull
    private String file_size;

    @NonNull
    private String encryption_key;

    private LocalDateTime created_at;


}
