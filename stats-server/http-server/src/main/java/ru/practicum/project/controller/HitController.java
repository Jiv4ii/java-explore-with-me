package ru.practicum.project.controller;


import com.sun.istack.NotNull;
import org.springframework.http.HttpStatus;
import ru.practicum.dto.HitDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.StatDto;
import ru.practicum.project.model.Hit;
import ru.practicum.project.service.HitService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class HitController {
    private final HitService service;

    @PostMapping("/hit")
    private ResponseEntity<Object> postHit(@Validated @RequestBody HitDto hitDto) {
        log.info("Обращение к методу postHit");
        return new ResponseEntity<>(service.createHit(hitDto), HttpStatus.CREATED);
    }


    @GetMapping("/stats")
    public ResponseEntity<List<StatDto>> getHit(@RequestParam @NotNull String start,
                                                @RequestParam @NotNull String end,
                                                @RequestParam(defaultValue = "") List<String> uris,
                                                @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Запрос статистики.");
        return new ResponseEntity<>(service.getStats(start, end, uris, unique), HttpStatus.OK);
    }


}
