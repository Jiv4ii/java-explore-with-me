package ru.practicum.project.requests.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.project.requests.model.ParticipationRequestDto;
import ru.practicum.project.requests.service.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class RequestController {
    private final RequestService service;

    @PostMapping("/users/{userId}/requests")
    public ResponseEntity<ParticipationRequestDto> createRequest(@PathVariable Long userId, @RequestParam long eventId) {
        return new ResponseEntity<>(service.createRequest(userId, eventId), HttpStatus.CREATED);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> updateRequest(@PathVariable Long userId, @PathVariable long requestId) {
        return new ResponseEntity<>(service.updateRequest(userId, requestId), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getUserRequests(@PathVariable Long userId) {
        return new ResponseEntity<>(service.getUsersRequest(userId), HttpStatus.OK);
    }
}
