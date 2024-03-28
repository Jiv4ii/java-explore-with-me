package ru.practicum.project.comments.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.project.comments.dto.CommentDto;
import ru.practicum.project.comments.dto.CommentMapper;
import ru.practicum.project.comments.dto.NewCommentDto;
import ru.practicum.project.comments.model.Comment;
import ru.practicum.project.comments.repository.CommentRepository;
import ru.practicum.project.events.service.EventService;
import ru.practicum.project.exceptions.ClosedEventGetException;
import ru.practicum.project.exceptions.CommentNotFoundException;
import ru.practicum.project.exceptions.UserNotCreatorExcepion;
import ru.practicum.project.users.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository repository;
    private final EventService eventService;
    private final UserService userService;

    @Transactional
    public CommentDto createComment(Long userId, Long eventId, NewCommentDto newCommentDto) {
        userService.checkExistsUser(userId);
        eventService.checkExistsEvent(eventId);
        if (eventService.getEventById(eventId).getPublishedOn() == null) {
            throw new ClosedEventGetException("Нельзя оставить комментарий к неопубликованному событию");
        }
        Comment comment = new Comment();
        comment.setText(newCommentDto.getText());
        comment.setCreator(userService.getUserById(userId));
        comment.setEvent(eventService.getEventById(eventId));
        comment.setPublished(LocalDateTime.now());


        return CommentMapper.toDto(repository.save(comment));
    }


    @Transactional(readOnly = true)
    public List<CommentDto> getUserComments(Long userId) {
        userService.checkExistsUser(userId);
        return repository.findCommentsByCreatorId(userId)
                .stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommentDto> getEventComments(Long eventId) {
        eventService.checkExistsEvent(eventId);
        return repository.findCommentsByEventId(eventId)
                .stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentDto updateComment(Long userId, Long comId, NewCommentDto updateComment) {
        userService.checkExistsUser(userId);
        checkExistComment(comId);
        Comment comment = repository.getReferenceById(comId);
        comment.setText(updateComment.getText());
        comment.setRedacted(LocalDateTime.now());

        return CommentMapper.toDto(repository.save(comment));
    }

    @Transactional
    public void deleteCommentUser(Long userId, Long comId) {
        userService.checkExistsUser(userId);
        checkExistComment(comId);
        if (repository.getReferenceById(comId).getCreator().getId() != userId) {
            throw new UserNotCreatorExcepion("Пользователь не является создателем комментария");
        }
        repository.deleteById(comId);
    }

    @Transactional
    public void deleteCommentAdmin(Long comId) {
        checkExistComment(comId);
        repository.deleteById(comId);
    }


    private void checkExistComment(long comId) {
        if (!repository.existsById(comId)) {
            throw new CommentNotFoundException("Комментарий не найден");
        }
    }
}
