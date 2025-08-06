package com.kozak.mybookshop.dto.book;

import com.kozak.mybookshop.validation.CoverImage;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateBookRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String author;
    @NotBlank
    @Size(min = 2, max = 10)
    private String isbn;
    @DecimalMin(value = "1.0", inclusive = false)
    private BigDecimal price;
    @Size(min = 2, max = 100)
    private String description;
    @CoverImage
    private String coverImage;
    @NotEmpty
    private List<Long> categoryIds;
}
