package ru.practicum.project.compilations.model.dto;

import lombok.*;
import ru.practicum.project.events.model.dto.EventShortDto;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {

    private long id;

    private Set<EventShortDto> events;

    private boolean pinned;

    private String title;
}
