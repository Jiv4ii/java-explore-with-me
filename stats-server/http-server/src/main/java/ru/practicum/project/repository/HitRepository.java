package ru.practicum.project.repository;

import ru.practicum.dto.StatDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.project.model.Hit;


import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Long> {

    @Query("select new ru.practicum.dto.StatDto(h.app, h.uri, COUNT(h.ip) ) " +
            "from Hit as h " +
            "where h.timestamp >= ?1 " +
            "and h.timestamp <= ?2 " +
            "group by h.app, h.uri " +
            "order by COUNT(h.ip) desc")
    List<StatDto> getStatsAllUrisAndNotUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.dto.StatDto(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "from Hit as h " +
            "where h.timestamp >= ?1 " +
            "and h.timestamp <= ?2 " +
            "group by h.app, h.uri " +
            "order by count(DISTINCT h.ip) desc")
    List<StatDto> getStatsAllUrisAndUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.dto.StatDto(h.app, h.uri, COUNT(h.ip)) " +
            "from Hit as h " +
            "where h.timestamp >= ?1 " +
            "and h.timestamp <= ?2 " +
            "and h.uri in ?3 " +
            "group by h.app, h.uri " +
            "order by count (h.ip) desc")
    List<StatDto> getStatsUrisAndNotUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.dto.StatDto(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "from Hit as h " +
            "where h.timestamp >= ?1 " +
            "and h.timestamp <= ?2 " +
            "and h.uri in ?3 " +
            "group by h.app, h.uri " +
            "order by count (DISTINCT h.ip) desc")
    List<StatDto> getStatsUrisAndUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris);
}