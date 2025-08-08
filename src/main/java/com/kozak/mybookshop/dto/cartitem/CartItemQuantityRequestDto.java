package com.kozak.mybookshop.dto.cartitem;

import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CartItemQuantityRequestDto {
    @Positive
    private int quantity;
}
