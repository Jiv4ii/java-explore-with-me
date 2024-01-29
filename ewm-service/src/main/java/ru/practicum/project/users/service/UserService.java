package ru.practicum.project.users.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.project.exceptions.DuplicateEmailException;
import ru.practicum.project.exceptions.UserNotFoundException;
import ru.practicum.project.users.model.NewUserRequest;
import ru.practicum.project.users.model.User;
import ru.practicum.project.users.model.UserDto;
import ru.practicum.project.users.model.UserMapper;
import ru.practicum.project.users.repository.UserRepository;

import org.springframework.data.domain.Page;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository repository;

    @Transactional
    public User createUser(NewUserRequest newUserRequest) {
        if (repository.findByEmail(newUserRequest.getEmail()) == null) {
            log.info("Пользователь успешно сохранен");
            return repository.save(UserMapper.toUser(newUserRequest));
        }
        throw new DuplicateEmailException("Пользователь с данным email уже существует");
    }

    @Transactional
    public List<UserDto> getUsers(Long[] ids, int from, int size) {
        if (ids != null) {
            log.info("Список пользователей получен");
            return repository.findAllById(Arrays.asList(ids)).stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        }
        Pageable pageable = PageRequest.of(from, size);
        Page<User> page = repository.findAll(pageable);
        log.info("Список пользователей получен");
        return page.getContent()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public User getUserById(Long userId) {
        checkExistsUser(userId);
        log.info("Пользователь успешно получен");
        return repository.getReferenceById(userId);
    }

    @Transactional
    public void deleteUserById(Long userId) {
        checkExistsUser(userId);
        repository.deleteById(userId);
        log.info("Пользователь успешно удален");
    }

    public void checkExistsUser(long userId) {
        if (!repository.existsById(userId)) {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }

}
