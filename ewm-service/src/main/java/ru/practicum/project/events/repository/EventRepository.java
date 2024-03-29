package ru.practicum.project.events.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.project.events.model.StateEvent;
import ru.practicum.project.events.model.Event;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findByInitiatorId(Long initiatorId, Pageable pageable);

    List<Event> findByCategoryId(Integer categoryId);

    @Query("select e from Event e " +
            "where e.state = ?1 " +
            "and ((upper(e.annotation) like upper(concat('%', ?2, '%'))) " +
            "or (upper(e.description) like upper(concat('%', ?2, '%')))) " +
            "and (e.category.id in ?3 or ?3 is null) " +
            "and (e.paid = ?4 or ?4 = null) " +
            "and (e.eventDate > ?5 or cast(?5 as LocalDateTime) is null) " +
            "and (e.eventDate < ?6 or cast(?6 as LocalDateTime) is null)")
    List<Event> publicSearchAllEvents(StateEvent state, String text, List<Integer> categories, Boolean paid,
                                      LocalDateTime start, LocalDateTime end, PageRequest page);

    @Query("select e from Event e " +
            "where (e.initiator.id in :users or :users is null) " +
            "and (e.state in :states or :states is null) " +
            "and (e.category.id in :idsCategory or :idsCategory is null) " +
            "and (e.eventDate > :rangeStart or cast(:rangeStart as LocalDateTime) is null) " +
            "and (e.eventDate < :rangeEnd or cast(:rangeEnd as LocalDateTime) is null)")
    List<Event> adminSearchEvents(List<Long> users,
                                  List<StateEvent> states,
                                  List<Long> idsCategory,
                                  LocalDateTime rangeStart,
                                  LocalDateTime rangeEnd,
                                  PageRequest page);

}
