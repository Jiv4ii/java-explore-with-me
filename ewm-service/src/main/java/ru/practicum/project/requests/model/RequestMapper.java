package ru.practicum.project.requests.model;

public final class RequestMapper {

    private RequestMapper() {
    }

    public static ParticipationRequestDto toRequestDto(Request request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus().toString())
                .build();
    }
}
