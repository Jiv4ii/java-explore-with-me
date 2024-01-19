package ru.practicum.ewmservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainServiceController {
    @GetMapping("/server")
    public ResponseEntity<Object> getHit() {
        return new ResponseEntity<>("Ответ.", HttpStatus.OK);
    }
}