package ru.practicum.project.compilations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.project.compilations.model.NewCompilationDto;
import ru.practicum.project.compilations.model.UpdateCompilationRequest;
import ru.practicum.project.compilations.service.CompilationService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class CompilationController {
    private final CompilationService service;


    @PostMapping("/admin/compilations")
    public ResponseEntity<Object> createCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.info("Запрос на создание подборки. " +
                "event: " + newCompilationDto.getEvents());
        return new ResponseEntity<>(service.createCompilation(newCompilationDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    public ResponseEntity<Object> deleteCompilation(@PathVariable long compId) {
        log.info("Запрос на удаление подборки id = {}", compId);
        service.deleteCompilation(compId);
        return new ResponseEntity<>("Подборка удалена.", HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/admin/compilations/{compId}")
    public ResponseEntity<Object> updateCompilation(@PathVariable long compId,
                                                    @RequestBody @Valid UpdateCompilationRequest updateCompilationRequest) {
        log.info("Запрос на обновление подборки id = {}", compId);
        return new ResponseEntity<>(service.updateCompilation(compId, updateCompilationRequest), HttpStatus.OK);
    }

    @GetMapping("/compilations")
    public ResponseEntity<Object> getCompilations(@RequestParam(defaultValue = "false") boolean pinned,
                                                  @RequestParam(defaultValue = "0") int from,
                                                  @RequestParam(defaultValue = "10") int size) {
        log.info("Запрос подборки событий, pinned = {}, from = {}, size = {}", pinned, from, size);
        return new ResponseEntity<>(service.getCompilations(pinned, from, size), HttpStatus.OK);
    }

    @GetMapping("/compilations/{compId}")
    public ResponseEntity<Object> getCompilationById(@PathVariable long compId) {
        log.info("Запрос подборки по id = {}", compId);
        return new ResponseEntity<>(service.getCompilationById(compId), HttpStatus.OK);
    }
}
