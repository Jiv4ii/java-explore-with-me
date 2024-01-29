package ru.practicum.project.categories.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.project.categories.model.Category;
import ru.practicum.project.categories.model.CategoryDto;
import ru.practicum.project.categories.service.CategoryService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Validated
public class CategoryController {
    private final CategoryService service;

    @PostMapping("/admin/categories")
    public ResponseEntity<Category> postCategory(@RequestBody @Valid CategoryDto categoryDto) {
        return new ResponseEntity<>(service.createCategory(categoryDto), HttpStatus.CREATED);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getCategories(@RequestParam(required = false, defaultValue = "0") Integer from,
                                                           @RequestParam(required = false, defaultValue = "10") Integer size) {
        return new ResponseEntity<>(service.getCategories(from, size), HttpStatus.OK);
    }

    @GetMapping("/categories/{catId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Integer catId) {
        return new ResponseEntity<>(service.getCategoryDtoById(catId), HttpStatus.OK);
    }

    @DeleteMapping("/admin/categories/{catId}")
    public ResponseEntity<Object> deleteCategory(@PathVariable Integer catId) {
        service.deleteCategoryrById(catId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/admin/categories/{catId}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Integer catId, @RequestBody @Valid CategoryDto categoryDto) {
        return new ResponseEntity<>(service.updateCategory(catId, categoryDto), HttpStatus.OK);
    }
}
