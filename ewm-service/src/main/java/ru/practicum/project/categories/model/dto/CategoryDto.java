package ru.practicum.project.categories.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.*;
import org.springframework.stereotype.Component;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Component
public class CategoryDto {

    private int id;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 50, message = "Длина имени должна быть от 1 до 50 символов.")
    private String name;
}