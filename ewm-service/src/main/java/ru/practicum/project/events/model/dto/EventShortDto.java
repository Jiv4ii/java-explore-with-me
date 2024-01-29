package ru.practicum.project.events.model.dto;

import lombok.*;
import ru.practicum.project.categories.model.CategoryDto;
import ru.practicum.project.users.model.UserShortDto;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {
    private long id;

    private String annotation;

    private CategoryDto category;

    private String eventDate;

    private boolean paid;

    private int confirmedRequests;

    private UserShortDto initiator;

    private String title;

    private int views;
}
