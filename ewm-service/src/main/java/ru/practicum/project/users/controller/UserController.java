package ru.practicum.project.users.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.project.users.model.NewUserRequest;
import ru.practicum.project.users.model.User;
import ru.practicum.project.users.model.UserDto;
import ru.practicum.project.users.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
@Validated
public class UserController {
    private final UserService service;

    @PostMapping
    public ResponseEntity<User> postUser(@RequestBody @Valid NewUserRequest newUserRequest){
        return  new ResponseEntity<>(service.createUser(newUserRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(@RequestParam(required = false) Long[] ids,
                                                  @RequestParam(required = false, defaultValue = "0")  Integer from,
                                                  @RequestParam(required = false, defaultValue = "10") Integer size){
        return  new ResponseEntity<>(service.getUsers(ids, from, size), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long userId){
        service.deleteUserById(userId);
        return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
