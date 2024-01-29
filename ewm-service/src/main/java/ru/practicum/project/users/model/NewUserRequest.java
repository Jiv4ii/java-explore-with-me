package ru.practicum.project.users.model;

import javax.validation.constraints.*;

import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewUserRequest {

    @Email
    @NotBlank
    @Size(min = 6, max = 254, message = "Длина эл.почты должна быть в диапозоне 6 - 254.")
    private String email;

    @NotBlank
    @Size(min = 2, max = 250, message = "Длина имени должна быть в диапозоне 2 - 250")
    private String name;
}
