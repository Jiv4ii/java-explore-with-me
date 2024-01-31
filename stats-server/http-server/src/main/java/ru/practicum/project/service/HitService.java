package ru.practicum.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.project.constants.Constants;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatDto;
import ru.practicum.project.exceptions.DateTimeException;
import ru.practicum.project.mapper.HitMapper;
import ru.practicum.project.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HitService {
    private final HitRepository repository;

    public HitDto createHit(HitDto hitDto) {
        return HitMapper.toHitDto(repository.save(HitMapper.toHit(hitDto)));
    }


    public List<StatDto> getStats(String strStart, String strEnd, List<String> uris, Boolean unique) {
        LocalDateTime start = parseDataTime(strStart);
        LocalDateTime end = parseDataTime(strEnd);


        if (start.equals(end) || start.isAfter(end)) {
            throw new DateTimeException("Некорректные даты в запросе.");
        }
        if (uris.isEmpty() && !unique) {
            return repository.getStatsAllUrisAndNotUniqueIp(start, end);
        }

        if (uris.isEmpty()) {
            return repository.getStatsAllUrisAndUniqueIp(start, end);
        }

        if (!unique) {
            return repository.getStatsUrisAndNotUniqueIp(start, end, uris);
        }

        return repository.getStatsUrisAndUniqueIp(start, end, uris);
    }

    private LocalDateTime parseDataTime(String value) {
        try {
            return LocalDateTime.parse(value, Constants.FORMATTER);
        } catch (RuntimeException e) {
            throw new DateTimeException("Дата должна быть в формате yyyy-MM-dd HH:mm:ss");
        }
    }
}
