package ru.practicum.project.events.service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.project.StatClient;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatDto;
import ru.practicum.project.categories.model.Category;
import ru.practicum.project.categories.service.CategoryService;
import ru.practicum.project.constants.Constants;
import ru.practicum.project.events.model.dto.EventMapper;
import ru.practicum.project.events.model.StateAction;
import ru.practicum.project.events.model.StateEvent;
import ru.practicum.project.events.model.dto.*;
import ru.practicum.project.events.model.Event;
import ru.practicum.project.events.model.SortEvent;
import ru.practicum.project.events.model.location.Location;
import ru.practicum.project.events.model.location.LocationMapper;
import ru.practicum.project.events.repository.EventRepository;
import ru.practicum.project.events.repository.LocationRepository;
import ru.practicum.project.exceptions.*;
import ru.practicum.project.requests.model.*;
import ru.practicum.project.requests.repository.RequestRepository;
import ru.practicum.project.users.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {
    private final EventRepository repository;
    private final CategoryService categoryService;
    private final UserService userService;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;
    private final StatClient statClient;


    @Transactional
    public EventFullDto createEvent(NewEventDto newEventDto, Long userId) {
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new DateTimeException("Начало события должно быть больше чем через два часа от текущего момента");
        }
        Event event = EventMapper.toEvent(newEventDto);
        event.setCategory(categoryService.getCategoryById(newEventDto.getCategory()));
        event.setInitiator(userService.getUserById(userId));
        event.setCreatedOn(LocalDateTime.now());
        event.setState(StateEvent.PENDING);
        Location location = locationRepository.save(LocationMapper.toLocation(newEventDto.getLocation()));
        event.setLocation(location);

        log.info("Событие успешно сохранено " + event);
        return EventMapper.toEventFullDto(repository.save(event));
    }

    public List<EventFullDto> getEvents() {
        return repository.findAll()
                .stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EventFullDto updateEventAdmin(UpdateEventAdminRequest updateEventAdminRequest, Long eventId) {
        checkExistsEvent(eventId);
        Event event = getEventById(eventId);

        if (!event.getState().equals(StateEvent.PENDING)) {
            throw new EventPublishedException("Событие не может быть опубликовано");
        }

        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }

        if (updateEventAdminRequest.getCategory() != null) {
            categoryService.checkExistCategory(updateEventAdminRequest.getCategory());
            Category category = categoryService.getCategoryById(updateEventAdminRequest.getCategory());
            event.setCategory(category);
        }

        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }

        if (updateEventAdminRequest.getEventDate() != null) {
            if (updateEventAdminRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new DateTimeException("Время начала события задано неверно");
            } else {
                event.setEventDate(updateEventAdminRequest.getEventDate());
            }
        }

        if (updateEventAdminRequest.getLocation() != null) {
            Location location = locationRepository.save(LocationMapper.toLocation(updateEventAdminRequest.getLocation()));
            event.setLocation(location);
        }

        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }

        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }

        if (updateEventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }

        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }

        if (updateEventAdminRequest.getStateAction() != null && updateEventAdminRequest.getStateAction() == StateAction.PUBLISH_EVENT) {
            event.setState(StateEvent.PUBLISHED);
            event.setPublishedOn(LocalDateTime.now());
        } else if (updateEventAdminRequest.getStateAction() != null && updateEventAdminRequest.getStateAction() == StateAction.REJECT_EVENT) {
            event.setState(StateEvent.CANCELED);
        }
        log.info("Событие успешно обновлено " + event);

        return EventMapper.toEventFullDto(repository.save(event));

    }

    @Transactional
    public EventFullDto updateEventUser(UpdateEventUserRequest updateEventUserRequest, Long userId, Long eventId) {
        checkExistsEvent(eventId);
        userService.checkExistsUser(userId);
        if (getEventById(eventId).getInitiator().getId() != userId) {
            log.info("Пользователь не является создателем события");
            throw new UserNotCreatorExcepion("Пользователь не является создателем события");
        }
        Event event = getEventById(eventId);

        if (event.getState().equals(StateEvent.PUBLISHED)) {
            log.info("Событие не может быть Обновлено");
            throw new EventPublishedException("Событие не может быть Обновлено");
        }

        if (updateEventUserRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventUserRequest.getAnnotation());
        }

        if (updateEventUserRequest.getCategory() != null) {
            categoryService.checkExistCategory(updateEventUserRequest.getCategory());
            Category category = categoryService.getCategoryById(updateEventUserRequest.getCategory());
            event.setCategory(category);
        }

        if (updateEventUserRequest.getDescription() != null) {
            event.setDescription(updateEventUserRequest.getDescription());
        }

        if (updateEventUserRequest.getEventDate() != null) {
            if (updateEventUserRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                log.info("Время начала события задано неверно");
                throw new DateTimeException("Время начала события задано неверно");
            } else {
                event.setEventDate(updateEventUserRequest.getEventDate());
            }
        }

        if (updateEventUserRequest.getLocation() != null) {
            Location location = locationRepository.save(LocationMapper.toLocation(updateEventUserRequest.getLocation()));
            event.setLocation(location);
        }

        if (updateEventUserRequest.getPaid() != null) {
            event.setPaid(updateEventUserRequest.getPaid());
        }

        if (updateEventUserRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        }

        if (updateEventUserRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventUserRequest.getRequestModeration());
        }

        if (updateEventUserRequest.getTitle() != null) {
            event.setTitle(updateEventUserRequest.getTitle());
        }

        if (updateEventUserRequest.getStateAction() != null && (updateEventUserRequest.getStateAction() == StateAction.CANCEL_REVIEW || updateEventUserRequest.getStateAction() == StateAction.SEND_TO_REVIEW)) {
            event.setState(StateEvent.PENDING);
        }

        if (updateEventUserRequest.getStateAction() != null && (updateEventUserRequest.getStateAction() == StateAction.CANCEL_REVIEW)) {
            event.setState(StateEvent.CANCELED);
        }


        log.info("Событие успешно обновлено " + event);

        return EventMapper.toEventFullDto(repository.save(event));

    }

    public List<EventShortDto> getUserEvents(long userId, int from, int size) {

        Pageable pageable = PageRequest.of(from, size);
        Page<Event> page = repository.findByInitiatorId(userId, pageable);
        return page.getContent().stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    public void checkExistsEvent(long eventId) {
        if (!repository.existsById(eventId)) {
            throw new EventNotFoundException("Событие не найдено");
        }
    }

    public Event getEventById(long eventId) {
        checkExistsEvent(eventId);

        return repository.getReferenceById(eventId);
    }

    public EventFullDto getEventDtoById(long eventId, HttpServletRequest request) {
        checkExistsEvent(eventId);
        Event event = repository.getReferenceById(eventId);
        if (event.getPublishedOn() == null) {
            throw new ClosedEventGetException("Событие не опубликовано");
        }
        createHit(request);
        event.setViews((int) getView(eventId));
        return EventMapper.toEventFullDto(repository.getReferenceById(eventId));
    }

    public EventFullDto getUserEventById(long eventId, long userId) {
        checkExistsEvent(eventId);
        userService.checkExistsUser(userId);
        if (repository.getReferenceById(eventId).getInitiator().getId() != userId) {
            throw new UserNotCreatorExcepion("Пользователь не является создателем события");
        }
        return EventMapper.toEventFullDto(repository.getReferenceById(eventId));
    }

    public EventRequestStatusUpdateResult changeRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        checkExistsEvent(eventId);
        userService.checkExistsUser(userId);
        Event event = getEventById(eventId);
        RequestStatus requestStatus = eventRequestStatusUpdateRequest.getStatus();
        List<ParticipationRequestDto> rejected = new ArrayList<>();
        List<ParticipationRequestDto> confirmed = new ArrayList<>();
        if (event.getInitiator().getId() != userId) {
            throw new UserNotCreatorExcepion("Пользователь не является создателем события");
        }
        if (event.getParticipantLimit() == 0 || event.isRequestModeration() == false) {
            return null;
        }
        if (event.getParticipantLimit() == event.getConfirmedRequests()) {
            throw new MaxLimitException("Событие заполненно");
        }
        List<Request> requests = requestRepository.findAllById(eventRequestStatusUpdateRequest.getRequestIds());
        for (Request request : requests) {
            if (request.getStatus() != RequestStatus.PENDING) {
                throw new RequestWrongStatusException("Статус запроса должен быть в состоянии PENDING");
            }
            if (event.getConfirmedRequests() == event.getParticipantLimit()) {
                requestStatus = RequestStatus.REJECTED;
            }
            request.setStatus(requestStatus);
            if (request.getStatus() == RequestStatus.CONFIRMED) {
                confirmed.add(RequestMapper.toRequestDto(request));
            } else {
                rejected.add(RequestMapper.toRequestDto(request));
            }

            if (requestStatus == RequestStatus.CONFIRMED) {
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                repository.save(event);
            }
            requestRepository.save(request);

        }
        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = new EventRequestStatusUpdateResult();
        eventRequestStatusUpdateResult.setConfirmedRequests(confirmed);
        eventRequestStatusUpdateResult.setRejectedRequests(rejected);
        return eventRequestStatusUpdateResult;
    }

    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {
        checkExistsEvent(eventId);
        userService.checkExistsUser(userId);
        if (getEventById(eventId).getInitiator().getId() != userId) {
            throw new UserNotCreatorExcepion("Пользователь не является создателем события");
        }
        return requestRepository.findByEventId(eventId).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    public List<EventFullDto> getEventsPublished(String text, List<Integer> categories, Boolean paid,
                                                 String rangeStart, String rangeEnd, boolean onlyAvailable,
                                                 SortEvent sort, int from, int size, HttpServletRequest request) {

        PageRequest page = PageRequest.of(from, size);
        LocalDateTime start = null;
        LocalDateTime end = null;

        if (rangeStart == null && rangeEnd == null) {
            start = LocalDateTime.now();
        }

        if (rangeStart != null) {
            start = parseDataTime(rangeStart);
        }

        if (rangeEnd != null) {
            end = parseDataTime(rangeEnd);
        }

        if (start != null && end != null && start.isAfter(end)) {
            throw new DateTimeException("Передана некорректная дата.");
        }

        List<Event> events = repository.publicSearchAllEvents(StateEvent.PUBLISHED, text, categories,
                paid, start, end, page);

        List<EventFullDto> eventFullDtos;

        if (onlyAvailable) {
            eventFullDtos = events.stream()
                    .filter(x -> x.getParticipantLimit() == 0 || x.getParticipantLimit() > x.getConfirmedRequests())
                    .map(EventMapper::toEventFullDto)
                    .collect(Collectors.toList());
        } else {
            eventFullDtos = events.stream()
                    .map(EventMapper::toEventFullDto)
                    .collect(Collectors.toList());
        }

        createHit(request);

        for (EventFullDto eventFullDto : eventFullDtos) {
            eventFullDto.setViews((int) getView(eventFullDto.getId()));
        }

        if (sort == SortEvent.EVENT_DATE) {
            List<EventFullDto> eventFullDtosSorted = eventFullDtos.stream()
                    .sorted(Comparator.comparing(EventFullDto::getEventDate))
                    .collect(Collectors.toList());

            return eventFullDtosSorted;

        } else if (sort == SortEvent.VIEWS) {
            List<EventFullDto> eventFullDtosSorted = eventFullDtos.stream()
                    .sorted(Comparator.comparingInt(EventFullDto::getViews))
                    .collect(Collectors.toList());

            return eventFullDtosSorted;
        }

        return eventFullDtos;
    }

    public List<EventFullDto> adminSearchEvents(List<Long> users, List<StateEvent> states, List<Long> idsCategory,
                                                String rangeStart, String rangeEnd, int from, int size) {

        PageRequest page = PageRequest.of(from, size);
        LocalDateTime start = null;
        LocalDateTime end = null;

        if (rangeStart != null) {
            start = parseDataTime(rangeStart);
        }

        if (rangeEnd != null) {
            end = parseDataTime(rangeEnd);
        }


        return repository.adminSearchEvents(users, states, idsCategory, start, end, page).stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());

    }

    private void createHit(HttpServletRequest request) {
        statClient.createStat(HitDto.builder()
                .app("ewm-main-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(Constants.FORMATTER.format(LocalDateTime.now()))
                .build());
    }

    private LocalDateTime parseDataTime(String value) {
        try {
            return LocalDateTime.parse(value, Constants.FORMATTER);
        } catch (DateTimeException e) {
            throw new DateTimeException("Дата должна быть в формате yyyy-MM-dd HH:mm:ss");
        }
    }

    private long getView(long eventId) {
        List<String> uris = List.of(String.format("/events/%s", eventId));

        ResponseEntity<Object> responseEntity = statClient.getStat(LocalDateTime.now().minusYears(1).format(Constants.FORMATTER),
                LocalDateTime.now().format(Constants.FORMATTER), uris, true);


        ObjectMapper mapper = new ObjectMapper();
        JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, StatDto.class);
        List<StatDto> statDtos = mapper.convertValue(responseEntity.getBody(), type);

        return !statDtos.isEmpty() ? statDtos.get(0).getHits() : 0;
    }


}
