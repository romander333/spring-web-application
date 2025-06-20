package com.kozak.mybookshop.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CategoryRequestDto {
    @NotBlank
    @Length(min = 8, max = 50)
    private String name;
    @Length(max = 150)
    private String description;
}
