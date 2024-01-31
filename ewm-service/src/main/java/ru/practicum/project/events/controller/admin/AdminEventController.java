package ru.practicum.project.events.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.project.events.model.StateEvent;
import ru.practicum.project.events.model.dto.EventFullDto;
import ru.practicum.project.events.model.dto.UpdateEventAdminRequest;
import ru.practicum.project.events.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Validated
public class AdminEventController {
    private final EventService service;


    @PatchMapping("/admin/events/{eventId}")
    public ResponseEntity<EventFullDto> updateEventAdmin(@RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest, @PathVariable Long eventId) {
        return new ResponseEntity<>(service.updateEventAdmin(updateEventAdminRequest, eventId), HttpStatus.OK);
    }


    @GetMapping("/admin/events")
    public ResponseEntity<Object> searchEvents(@RequestParam(required = false) List<Long> users,
                                               @RequestParam(required = false) List<StateEvent> states,
                                               @RequestParam(required = false) List<Long> idsCategory,
                                               @RequestParam(required = false) String rangeStart,
                                               @RequestParam(required = false) String rangeEnd,
                                               @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                               @RequestParam(defaultValue = "10") @Positive int size) {
        return new ResponseEntity<>(service.adminSearchEvents(users, states, idsCategory, rangeStart, rangeEnd, from, size), HttpStatus.OK);
    }


}
