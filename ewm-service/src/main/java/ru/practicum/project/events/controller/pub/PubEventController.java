package ru.practicum.project.events.controller.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.project.events.model.SortEvent;
import ru.practicum.project.events.model.dto.EventFullDto;
import ru.practicum.project.events.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Validated
public class PubEventController {
    private final EventService service;


    @GetMapping("/events/{eventId}")
    public ResponseEntity<EventFullDto> getEvent(@PathVariable Long eventId, HttpServletRequest request) {
        return new ResponseEntity<>(service.getEventDtoById(eventId, request), HttpStatus.OK);
    }


    @GetMapping("/events")
    public ResponseEntity<Object> getEvents(@RequestParam(defaultValue = "") String text,
                                            @RequestParam(required = false) List<Integer> categories,
                                            @RequestParam(required = false) Boolean paid,
                                            @RequestParam(required = false) String rangeStart,
                                            @RequestParam(required = false) String rangeEnd,
                                            @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                            @RequestParam(required = false) SortEvent sort,
                                            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                            @RequestParam(defaultValue = "10") @Positive int size,
                                            HttpServletRequest request) {
        return new ResponseEntity<>(service.getEventsPublished(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size, request), HttpStatus.OK);
    }


}
