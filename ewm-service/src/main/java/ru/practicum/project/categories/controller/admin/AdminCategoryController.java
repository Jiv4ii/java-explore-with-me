package ru.practicum.project.categories.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.project.categories.model.Category;
import ru.practicum.project.categories.model.dto.CategoryDto;
import ru.practicum.project.categories.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Validated
public class AdminCategoryController {
    private final CategoryService service;

    @PostMapping("/admin/categories")
    public ResponseEntity<Category> postCategory(@RequestBody @Valid CategoryDto categoryDto) {
        return new ResponseEntity<>(service.createCategory(categoryDto), HttpStatus.CREATED);
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
