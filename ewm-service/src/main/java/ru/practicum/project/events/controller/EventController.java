package ru.practicum.project.events.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.project.events.model.StateEvent;
import ru.practicum.project.events.model.dto.*;
import ru.practicum.project.events.model.SortEvent;
import ru.practicum.project.events.service.EventService;
import ru.practicum.project.requests.model.EventRequestStatusUpdateRequest;
import ru.practicum.project.requests.model.EventRequestStatusUpdateResult;
import ru.practicum.project.requests.model.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Validated
public class EventController {
    private final EventService service;

    @PostMapping("/users/{userId}/events")
    public ResponseEntity<EventFullDto> postEvent(@RequestBody @Valid NewEventDto newEventDto, @PathVariable Long userId) {
        return new ResponseEntity<>(service.createEvent(newEventDto, userId), HttpStatus.CREATED);
    }

    @GetMapping("/users/events")
    public ResponseEntity<List<EventFullDto>> getEvents() {
        return new ResponseEntity<>(service.getEvents(), HttpStatus.OK);
    }

    @PatchMapping("/admin/events/{eventId}")
    public ResponseEntity<EventFullDto> updateEventAdmin(@RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest, @PathVariable Long eventId) {
        return new ResponseEntity<>(service.updateEventAdmin(updateEventAdminRequest, eventId), HttpStatus.OK);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public ResponseEntity<EventFullDto> updateEventUser(@RequestBody @Valid UpdateEventUserRequest updateEventUserRequest, @PathVariable Long userId, @PathVariable Long eventId) {
        return new ResponseEntity<>(service.updateEventUser(updateEventUserRequest, userId, eventId), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/events")
    public ResponseEntity<List<EventShortDto>> getUserEvents(@PathVariable Long userId, @RequestParam(required = false, defaultValue = "0") Integer from, @RequestParam(required = false, defaultValue = "10") Integer size) {
        return new ResponseEntity<>(service.getUserEvents(userId, from, size), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public ResponseEntity<EventFullDto> getUserEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return new ResponseEntity<>(service.getUserEventById(eventId, userId), HttpStatus.OK);
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<EventFullDto> getEvent(@PathVariable Long eventId, HttpServletRequest request) {
        return new ResponseEntity<>(service.getEventDtoById(eventId, request), HttpStatus.OK);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> changeRequestStatus(@PathVariable Long userId, @PathVariable Long eventId, @RequestBody @Valid EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        return new ResponseEntity<>(service.changeRequestStatus(userId, eventId, eventRequestStatusUpdateRequest), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return new ResponseEntity<>(service.getEventRequests(userId, eventId), HttpStatus.OK);
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
