package ru.practicum.project.categories.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.project.categories.model.Category;
import ru.practicum.project.categories.model.dto.CategoryDto;
import ru.practicum.project.categories.model.dto.CategoryMapper;
import ru.practicum.project.categories.repository.CategoryRepository;
import ru.practicum.project.events.repository.EventRepository;
import ru.practicum.project.exceptions.CategoryNotFoundException;
import ru.practicum.project.exceptions.DeleteCategoryException;
import ru.practicum.project.exceptions.DuplicateCategoryNameException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
    private final CategoryRepository repository;
    private final EventRepository eventRepository;

    @Transactional
    public Category createCategory(CategoryDto categoryDto) {
        if (repository.findByName(categoryDto.getName()) == null) {
            log.info("Категория успешно сохранена");
            return repository.save(CategoryMapper.toCategory(categoryDto));
        }
        throw new DuplicateCategoryNameException("Категория с данным именем уже существует");
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getCategories(int from, int size) {
        Pageable pageable = PageRequest.of(from, size);
        Page<Category> page = repository.findAll(pageable);
        log.info("Список категорий получен");
        return page.getContent()
                .stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDto getCategoryDtoById(Integer catId) {
        checkExistCategory(catId);
        return CategoryMapper.toCategoryDto(repository.getReferenceById(catId));
    }

    @Transactional
    public Category getCategoryById(Integer catId) {
        checkExistCategory(catId);
        return repository.getReferenceById(catId);
    }

    @Transactional
    public void deleteCategoryrById(Integer catId) {
        checkExistCategory(catId);

        if (eventRepository.findByCategoryId(catId).isEmpty()) {
            repository.deleteById(catId);
            log.info("Категория успешно удалена");
            return;
        }

        throw new DeleteCategoryException("Категорию нельзя удалить, имеются связанные события");
    }

    @Transactional
    public CategoryDto updateCategory(Integer catId, CategoryDto categoryDto) {
        checkExistCategory(catId);
        if (getCategoryById(catId).getName().equals(categoryDto.getName())) {
            return CategoryMapper.toCategoryDto(getCategoryById(catId));
        }
        if (repository.findByName(categoryDto.getName()) == null) {
            log.info("Категория успешно обновлена");
            Category newCategory = repository.getReferenceById(catId);
            newCategory.setName(categoryDto.getName());
            return CategoryMapper.toCategoryDto(repository.save(newCategory));
        }
        throw new DuplicateCategoryNameException("Категория с данным именем уже существует");
    }

    public void checkExistCategory(int catId) {
        if (!repository.existsById(catId)) {
            throw new CategoryNotFoundException("Категория не найдена");
        }
    }

}
