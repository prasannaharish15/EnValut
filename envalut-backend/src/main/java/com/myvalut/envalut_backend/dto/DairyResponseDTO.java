package com.myvalut.envalut_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DairyResponseDTO {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdOn;
}
