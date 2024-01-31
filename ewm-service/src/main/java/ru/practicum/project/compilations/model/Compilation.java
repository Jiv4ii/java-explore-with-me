package ru.practicum.project.compilations.model;

import lombok.*;
import ru.practicum.project.events.model.Event;

import javax.persistence.*;
import java.util.Set;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "compilations")
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToMany
    @JoinTable(name = "events_compilations",
            joinColumns = @JoinColumn(name = "compillation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private Set<Event> events;

    @Column(nullable = false)
    private boolean pinned;

    @Column(nullable = false)
    private String title;
}
