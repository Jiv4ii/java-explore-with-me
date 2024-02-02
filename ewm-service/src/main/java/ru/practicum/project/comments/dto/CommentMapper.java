package ru.practicum.project.comments.dto;

import ru.practicum.project.comments.model.Comment;
import ru.practicum.project.events.model.dto.EventMapper;
import ru.practicum.project.users.model.UserMapper;

public final class CommentMapper {

    private CommentMapper() {
    }

    public static CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .creator(UserMapper.toUserShortDto(comment.getCreator()))
                .event(EventMapper.toEventShortDto(comment.getEvent()))
                .published(comment.getPublished())
                .redacted(comment.getRedacted())
                .text(comment.getText())
                .id(comment.getId())
                .build();
    }
}
