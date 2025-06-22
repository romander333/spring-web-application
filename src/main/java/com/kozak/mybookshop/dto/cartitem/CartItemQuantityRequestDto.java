package com.kozak.mybookshop.dto.cartitem;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CartItemQuantityRequestDto {
    @Positive
    private int quantity;
}
