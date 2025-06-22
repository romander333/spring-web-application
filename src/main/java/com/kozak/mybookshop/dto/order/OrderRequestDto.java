package com.kozak.mybookshop.dto.order;

import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderRequestDto {
    @NotBlank
    private String shippingAddress;
}
