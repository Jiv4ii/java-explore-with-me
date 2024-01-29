package ru.practicum.project.events.model;


import ru.practicum.project.categories.model.CategoryMapper;
import ru.practicum.project.constants.Constants;
import ru.practicum.project.events.model.dto.EventFullDto;
import ru.practicum.project.events.model.dto.EventShortDto;
import ru.practicum.project.events.model.dto.NewEventDto;
import ru.practicum.project.events.model.location.LocationMapper;
import ru.practicum.project.users.model.UserMapper;

public class EventMapper {

    public static Event toEvent(NewEventDto newEventDto) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .paid(newEventDto.isPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.isRequestModeration())
                .title(newEventDto.getTitle())
                .build();
    }

    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .description(event.getDescription())
                .createdOn(event.getCreatedOn().format(Constants.FORMATTER))
                .eventDate(event.getEventDate().format(Constants.FORMATTER))
                .publishedOn(event.getPublishedOn() != null ? event.getPublishedOn().format(Constants.FORMATTER) : null)
                .location(LocationMapper.toLocationDto(event.getLocation()))
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .confirmedRequests(event.getConfirmedRequests())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .requestModeration(event.isRequestModeration())
                .title(event.getTitle())
                .state(event.getState())
                .views(event.getViews())
                .build();
    }

    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .eventDate(event.getEventDate().format(Constants.FORMATTER))
                .paid(event.isPaid())
                .confirmedRequests(event.getConfirmedRequests())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }
}
