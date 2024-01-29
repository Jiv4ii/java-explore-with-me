package ru.practicum.project.requests.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.project.events.model.StateEvent;
import ru.practicum.project.events.model.Event;
import ru.practicum.project.events.repository.EventRepository;
import ru.practicum.project.events.service.EventService;
import ru.practicum.project.exceptions.*;
import ru.practicum.project.requests.model.*;
import ru.practicum.project.requests.repository.RequestRepository;
import ru.practicum.project.users.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestService {
    private final RequestRepository repository;
    private final UserService userService;
    private final EventService eventService;
    private final EventRepository eventRepository;

    @Transactional
    public ParticipationRequestDto createRequest(long userId, long eventId) {
        checkRequestForPost(userId, eventId);
        Request request = new Request();
        Event event = eventService.getEventById(eventId);
        request.setCreated(LocalDateTime.now());
        request.setRequester(userService.getUserById(userId));
        request.setEvent(eventService.getEventById(eventId));
        request.setStatus(RequestStatus.PENDING);
        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }
        return RequestMapper.toRequestDto(repository.save(request));
    }

    @Transactional
    public ParticipationRequestDto updateRequest(long userId, long requestId) {
        checkExistRequest(requestId);
        Request request = getRequestById(requestId);
        if (request.getRequester().getId() != userId) {
            throw new UserNotCreatorExcepion("Пользователь не является создателем запроса");
        }
        request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toRequestDto(repository.save(request));
    }

    @Transactional
    public List<ParticipationRequestDto> getRequests() {
        return repository.findAll()
                .stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    void checkExistRequest(Long userId, Long eventId) {
        if (!repository.findByUserIdAndEventId(userId, eventId).isEmpty()) {
            throw new RequestAlreadyExistException("Нельзя отправлять повторные запросы");
        }
    }

    public void checkExistRequest(Long requestId) {
        if (!repository.existsById(requestId)) {
            throw new RequestNotFoundException("Запрос не найден");
        }
    }

    public void checkRequestForPost(Long userId, Long eventId) {
        userService.checkExistsUser(userId);
        eventService.checkExistsEvent(eventId);
        checkExistRequest(userId, eventId);

        if (eventService.getEventById(eventId).getInitiator().getId() == userId) {
            throw new CreatorCantRequestException("Создатель события не может отправлять заявку на участие");
        }

        if (eventService.getEventById(eventId).getState() != StateEvent.PUBLISHED) {
            throw new ClosedEventRequestException("Невозможно отправить запрос на участие в неопубликованном событии");
        }

        if (eventService.getEventById(eventId).getParticipantLimit() != 0 && (eventService.getEventById(eventId).getConfirmedRequests() == eventService.getEventById(eventId).getParticipantLimit())) {
            throw new MaxLimitException("Событие заполненно");
        }

    }

    @Transactional
    public Request getRequestById(Long requestId) {
        checkExistRequest(requestId);
        return repository.getReferenceById(requestId);
    }

    @Transactional
    public List<ParticipationRequestDto> getUsersRequest(Long userId) {
        userService.checkExistsUser(userId);
        return repository.findByRequesterId(userId).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

}
