package ru.practicum.project.compilations.model.dto;

import ru.practicum.project.compilations.model.Compilation;
import ru.practicum.project.events.model.dto.EventMapper;
import ru.practicum.project.events.model.dto.EventShortDto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class CompilationMapper {

    private CompilationMapper() {
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        List<EventShortDto> events = compilation.getEvents().stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
        return CompilationDto.builder()
                .events(Set.copyOf(events))
                .id(compilation.getId())
                .pinned(compilation.isPinned())
                .title(compilation.getTitle())
                .build();
    }

    public static Compilation toCompilation(NewCompilationDto newCompilationDto) {
        return Compilation.builder()
                .pinned(newCompilationDto.isPinned())
                .title(newCompilationDto.getTitle())
                .build();
    }
}
