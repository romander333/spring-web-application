package com.kozak.mybookshop.dto.order;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderStatusRequestDto {
    @NotBlank
    private String status;
}
