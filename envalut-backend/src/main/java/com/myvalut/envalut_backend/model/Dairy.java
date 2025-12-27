package com.myvalut.envalut_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Dairy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private Long userId;

    @Lob
    private byte[] encryptedTitle;

    @Lob
    private byte[] encryptedText;

    private String encryptedAesKey;

    private LocalDateTime createdOn;


}
