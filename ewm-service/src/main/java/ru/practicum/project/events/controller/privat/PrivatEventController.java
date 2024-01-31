package ru.practicum.project.events.controller.privat;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.project.events.model.dto.EventFullDto;
import ru.practicum.project.events.model.dto.EventShortDto;
import ru.practicum.project.events.model.dto.NewEventDto;
import ru.practicum.project.events.model.dto.UpdateEventUserRequest;
import ru.practicum.project.events.service.EventService;
import ru.practicum.project.requests.model.EventRequestStatusUpdateRequest;
import ru.practicum.project.requests.model.EventRequestStatusUpdateResult;
import ru.practicum.project.requests.model.ParticipationRequestDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Validated
public class PrivatEventController {
    private final EventService service;

    @PostMapping("/users/{userId}/events")
    public ResponseEntity<EventFullDto> postEvent(@RequestBody @Valid NewEventDto newEventDto, @PathVariable Long userId) {
        return new ResponseEntity<>(service.createEvent(newEventDto, userId), HttpStatus.CREATED);
    }

    @GetMapping("/users/events")
    public ResponseEntity<List<EventFullDto>> getEvents() {
        return new ResponseEntity<>(service.getEvents(), HttpStatus.OK);
    }


    @PatchMapping("/users/{userId}/events/{eventId}")
    public ResponseEntity<EventFullDto> updateEventUser(@RequestBody @Valid UpdateEventUserRequest updateEventUserRequest, @PathVariable Long userId, @PathVariable Long eventId) {
        return new ResponseEntity<>(service.updateEventUser(updateEventUserRequest, userId, eventId), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/events")
    public ResponseEntity<List<EventShortDto>> getUserEvents(@PathVariable Long userId, @RequestParam(defaultValue = "0") Integer from, @RequestParam(defaultValue = "10") Integer size) {
        return new ResponseEntity<>(service.getUserEvents(userId, from, size), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public ResponseEntity<EventFullDto> getUserEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return new ResponseEntity<>(service.getUserEventById(eventId, userId), HttpStatus.OK);
    }


    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> changeRequestStatus(@PathVariable Long userId, @PathVariable Long eventId, @RequestBody @Valid EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        return new ResponseEntity<>(service.changeRequestStatus(userId, eventId, eventRequestStatusUpdateRequest), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return new ResponseEntity<>(service.getEventRequests(userId, eventId), HttpStatus.OK);
    }


}
