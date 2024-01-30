package ru.practicum.project.events.model.dto;

import lombok.*;
import ru.practicum.project.categories.model.dto.CategoryDto;
import ru.practicum.project.events.model.StateEvent;
import ru.practicum.project.events.model.location.LocationDto;
import ru.practicum.project.users.model.UserShortDto;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {

    private long id;

    private String annotation;

    private CategoryDto category;

    private String description;

    private String createdOn;

    private String eventDate;

    private String publishedOn;

    private LocationDto location;

    private boolean paid;

    private int participantLimit;

    private int confirmedRequests;

    private UserShortDto initiator;

    private boolean requestModeration;

    private String title;

    private StateEvent state;

    private int views;
}
