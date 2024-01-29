package ru.practicum.project.events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.project.events.model.location.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
