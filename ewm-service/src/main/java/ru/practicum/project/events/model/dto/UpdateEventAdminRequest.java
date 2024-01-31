package ru.practicum.project.events.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.Valid;
import javax.validation.constraints.*;

import lombok.*;
import ru.practicum.project.events.model.StateAction;
import ru.practicum.project.events.model.location.LocationDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventAdminRequest {

    private StateAction stateAction;

    @Size(min = 20, max = 2000, message = "Длина аннотации должна быть в диапазоне 20-2000.")
    private String annotation;

    @Positive(message = "Значение категории не может быть отрицательным.")
    private Integer category;

    @Size(min = 20, max = 7000, message = "Длина описании должна быть в диапазоне 20-7000.")
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @Valid
    private LocationDto location;

    private Boolean paid;

    @PositiveOrZero
    private Integer participantLimit;

    private Boolean requestModeration;

    @Size(min = 3, max = 120, message = "Длина аннотации должна быть в диапазоне 3-120.")
    private String title;
}
