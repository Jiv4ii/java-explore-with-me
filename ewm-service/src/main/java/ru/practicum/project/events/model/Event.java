package ru.practicum.project.events.model;

import lombok.*;
import org.springframework.stereotype.Component;
import ru.practicum.project.categories.model.Category;
import ru.practicum.project.events.model.StateEvent;
import ru.practicum.project.events.model.location.Location;
import ru.practicum.project.users.model.User;

import javax.persistence.*;


import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "iventes")
@Component
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 2000)
    private String annotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;


    @Column(nullable = false, length = 7000)
    private String description;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column
    private boolean paid;

    @Column(name = "participant_limit")
    private int participantLimit;

    @Column(name = "confirmed_requests")
    private int confirmedRequests;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;


    @Column(name = "request_moderation", nullable = false)
    @Getter
    private boolean requestModeration;

    @Column(length = 120)
    private String title;

    @Column
    @Enumerated(EnumType.STRING)
    private StateEvent state;

    @Column
    private int views;

}
