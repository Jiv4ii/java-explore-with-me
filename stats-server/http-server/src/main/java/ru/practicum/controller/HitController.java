package ru.practicum.controller;


import com.sun.istack.NotNull;
import org.springframework.http.HttpStatus;
import ru.practicum.dto.HitDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.Hit;
import ru.practicum.service.HitService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class HitController {
    private final HitService service;

    @PostMapping("/hit")
    private ResponseEntity<Object> postHit(@Validated @RequestBody HitDto hitDto) {
        service.createHit(hitDto);
        return new ResponseEntity<>("Информация сохранена", HttpStatus.CREATED);
    }

    @GetMapping("/hit")
    private List<Hit> postHit() {
        return service.getHit();
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getHit(@RequestParam @NotNull String start,
                                         @RequestParam @NotNull String end,
                                         @RequestParam(defaultValue = "") List<String> uris,
                                         @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Запрос статистики.");
        return new ResponseEntity<>(service.getStats(start, end, uris, unique), HttpStatus.OK);
    }


}
