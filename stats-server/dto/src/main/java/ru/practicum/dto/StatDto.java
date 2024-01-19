package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatDto {
    private String app;
    private String uri;
    private long hits;
}
