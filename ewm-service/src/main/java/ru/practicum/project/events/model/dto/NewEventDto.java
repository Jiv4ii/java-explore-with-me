package ru.practicum.project.events.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;

import javax.validation.*;
import javax.validation.constraints.*;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.project.events.model.location.LocationDto;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000, message = "Длина аннотации должна быть в диапазоне 20-2000.")
    private String annotation;

    @NotNull
    @Positive(message = "Значение категории не может быть отрицательным.")
    private Integer category;

    @NotBlank
    @Size(min = 20, max = 7000, message = "Длина описании должна быть в диапазоне 20-7000.")
    private String description;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @Valid
    @NotNull
    private LocationDto location;

    private boolean paid;

    private int participantLimit;

    private boolean requestModeration = true;

    @NotBlank
    @Size(min = 3, max = 120, message = "Длина аннотации должна быть в диапазоне 3-120.")
    private String title;

}
