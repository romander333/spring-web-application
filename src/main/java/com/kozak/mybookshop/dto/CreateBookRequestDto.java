package com.kozak.mybookshop.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kozak.mybookshop.validation.CoverImage;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String author;
    @DecimalMin(value = "1.0", inclusive = false)
    private BigDecimal price;
    @Size(min = 2, max = 100)
    private String description;
    @CoverImage
    private String coverImage;
}
