package com.kozak.mybookshop.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

@Data
@Accessors(chain = true)
public class CategoryRequestDto {
    @NotBlank
    @Length(min = 4, max = 50)
    private String name;
    @Length(max = 150)
    private String description;
}
