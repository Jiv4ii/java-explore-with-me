package ru.practicum.project.events.model.location;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import lombok.*;


@Data
@Builder
public class LocationDto {

    @Min(-90)
    @Max(90)
    private float lat;

    @Min(-180)
    @Max(180)
    private float lon;
}
