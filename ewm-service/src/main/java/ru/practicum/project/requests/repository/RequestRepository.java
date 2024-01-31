package ru.practicum.project.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.project.requests.model.Request;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query("SELECT r FROM Request r WHERE r.requester.id = ?1 AND r.event.id = ?2")
    Optional<Request> findByUserIdAndEventId(Long userId, Long eventId);

    List<Request> findByEventId(long eventId);

    List<Request> findByRequesterId(long eventId);


}
