package ru.practicum.project.compilations.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.project.compilations.model.*;
import ru.practicum.project.compilations.model.dto.CompilationDto;
import ru.practicum.project.compilations.model.dto.CompilationMapper;
import ru.practicum.project.compilations.model.dto.NewCompilationDto;
import ru.practicum.project.compilations.model.dto.UpdateCompilationRequest;
import ru.practicum.project.compilations.repository.CompilationRepository;
import ru.practicum.project.events.model.Event;
import ru.practicum.project.events.service.EventService;
import ru.practicum.project.exceptions.CompilationNotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationService {
    private final CompilationRepository repository;
    private final EventService eventService;

    @Transactional
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);
        Set<Event> events = new HashSet<>();

        if (newCompilationDto.getEvents() != null) {
            for (long eventId : newCompilationDto.getEvents()) {
                events.add(eventService.getEventById(eventId));
            }
        }
        compilation.setEvents(events);

        log.info("Размер списка event после стрима: " + events.size());
        return CompilationMapper.toCompilationDto(repository.save(compilation));
    }

    @Transactional
    public void deleteCompilation(long compId) {
        checkExistsCompilation(compId);
        repository.deleteById(compId);
    }

    @Transactional
    public CompilationDto updateCompilation(long compId, UpdateCompilationRequest updateCompilationRequest) {
        checkExistsCompilation(compId);
        Compilation compilation = repository.getReferenceById(compId);

        if (updateCompilationRequest.getEvents() != null) {
            Set<Event> events = new HashSet<>();
            for (long eventId : updateCompilationRequest.getEvents()) {
                events.add(eventService.getEventById(eventId));
            }
            compilation.setEvents(events);
        }

        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }

        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }

        return CompilationMapper.toCompilationDto(repository.save(compilation));
    }

    @Transactional
    public List<CompilationDto> getCompilations(boolean pinned, int from, int size) {
        PageRequest page = PageRequest.of(from, size);
        return repository.findAllByPinned(pinned, page).stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CompilationDto getCompilationById(long compId) {
        checkExistsCompilation(compId);
        return CompilationMapper.toCompilationDto(repository.getReferenceById(compId));
    }

    private void checkExistsCompilation(long compId) {
        if (!repository.existsById(compId)) {
            throw new CompilationNotFoundException("Подборка не найдена");
        }
    }
}
