package ru.practicum.project.comments.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.project.events.model.dto.EventShortDto;
import ru.practicum.project.users.model.UserShortDto;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {


    private long id;


    private String text;


    private UserShortDto creator;


    private EventShortDto event;


    private LocalDateTime published;

    private LocalDateTime redacted;
}
