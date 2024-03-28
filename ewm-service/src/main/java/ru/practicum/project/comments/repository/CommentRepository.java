package ru.practicum.project.comments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.project.comments.model.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findCommentsByCreatorId(Long creatorId);

    List<Comment> findCommentsByEventId(Long creatorId);


}
