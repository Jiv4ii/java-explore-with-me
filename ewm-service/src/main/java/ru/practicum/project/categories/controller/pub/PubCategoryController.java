package ru.practicum.project.categories.controller.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.project.categories.model.dto.CategoryDto;
import ru.practicum.project.categories.service.CategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Validated
public class PubCategoryController {
    private final CategoryService service;


    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getCategories(@RequestParam(defaultValue = "0") Integer from,
                                                           @RequestParam(defaultValue = "10") Integer size) {
        return new ResponseEntity<>(service.getCategories(from, size), HttpStatus.OK);
    }

    @GetMapping("/categories/{catId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Integer catId) {
        return new ResponseEntity<>(service.getCategoryDtoById(catId), HttpStatus.OK);
    }

}
