package ru.practicum.project.compilations.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.project.compilations.service.CompilationService;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class PubCompilationController {
    private final CompilationService service;


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
