package ru.practicum.project.comments.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.project.comments.dto.CommentDto;
import ru.practicum.project.comments.dto.NewCommentDto;
import ru.practicum.project.comments.service.CommentService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequiredArgsConstructor
@Validated
public class CommentController {

    private final CommentService service;

    @PostMapping("/users/{userId}/events/{eventId}/comments")
    public ResponseEntity<CommentDto> postComment(@PathVariable Long userId, @PathVariable Long eventId, @RequestBody @Valid NewCommentDto newCommentDto) {
        return new ResponseEntity<>(service.createComment(userId, eventId, newCommentDto), HttpStatus.CREATED);
    }

    @GetMapping("/users/{userId}/comments")
    public ResponseEntity<List<CommentDto>> getUserComments(@PathVariable Long userId) {
        return new ResponseEntity<>(service.getUserComments(userId), HttpStatus.OK);
    }

    @GetMapping("/events/{eventId}/comments")
    public ResponseEntity<List<CommentDto>> getEventComments(@PathVariable Long eventId) {
        return new ResponseEntity<>(service.getEventComments(eventId), HttpStatus.OK);
    }

    @PatchMapping("/users/{userId}/comments/{comId}")
    public ResponseEntity<CommentDto> updateEventComments(@PathVariable Long userId, @PathVariable Long comId, @RequestBody @Valid NewCommentDto updateComment) {
        return new ResponseEntity<>(service.updateComment(userId, comId, updateComment), HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}/comments/{comId}")
    public ResponseEntity<Object> deleteUserComment(@PathVariable Long userId, @PathVariable Long comId) {
        service.deleteCommentUser(userId, comId);
        return new ResponseEntity<>("Комментарий удален", HttpStatus.OK);
    }

    @DeleteMapping("/admin/comments/{comId}")
    public ResponseEntity<Object> deleteUserComment(@PathVariable Long comId) {
        service.deleteCommentAdmin(comId);
        return new ResponseEntity<>("Комментарий удален", HttpStatus.OK);
    }
}
