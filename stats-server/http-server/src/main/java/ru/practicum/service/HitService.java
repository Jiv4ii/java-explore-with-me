package ru.practicum.service;

import ru.practicum.dto.HitDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.mapper.HitMapper;
import ru.practicum.model.Hit;
import ru.practicum.repository.HitRepository;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HitService {
    private final HitRepository repository;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ResponseEntity<Object> createHit(HitDto hitDto) {
        repository.save(HitMapper.toHit(hitDto));
        return new ResponseEntity<>("Информация сохранена", HttpStatus.CREATED);
    }

    public List<Hit> getHit() {
        return repository.findAll();
    }

    public ResponseEntity<Object> getStats(String strStart, String strEnd, List<String> uris, Boolean unique) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime start = LocalDateTime.parse(strStart, formatter);
//        LocalDateTime end = LocalDateTime.parse(strEnd, formatter);
        LocalDateTime start = parseDataTime(strStart);
        LocalDateTime end = parseDataTime(strEnd);


        if (start.equals(end) || start.isAfter(end)) {
            throw new DateTimeException("Некорректные даты в запросе.");
        }
        if (uris.isEmpty() && !unique) {
            return new ResponseEntity<>(repository.getStatsAllUrisAndNotUniqueIp(start, end), HttpStatus.OK);
        }

        if (uris.isEmpty() && unique) {
            return new ResponseEntity<>(repository.getStatsAllUrisAndUniqueIp(start, end), HttpStatus.OK);
        }

        if (!unique) {
            return new ResponseEntity<>(repository.getStatsUrisAndNotUniqueIp(start, end, uris), HttpStatus.OK);
        }

        return new ResponseEntity<>(repository.getStatsUrisAndUniqueIp(start, end, uris), HttpStatus.OK);
    }

    private LocalDateTime parseDataTime(String value) {
        try {
            return LocalDateTime.parse(value, FORMATTER);
        } catch (RuntimeException e) {
            throw new DateTimeException("Дата должна быть в формате yyyy-MM-dd HH:mm:ss");
        }
    }
}
