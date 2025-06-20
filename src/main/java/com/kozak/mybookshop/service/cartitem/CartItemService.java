package com.kozak.mybookshop.service.cartitem;

import com.kozak.mybookshop.dto.cartItem.CartItemDto;
import com.kozak.mybookshop.dto.cartItem.CreateCartItemRequestDto;
import java.util.List;

public interface CartItemService {
    CartItemDto save(CreateCartItemRequestDto requestDto);

    List<CartItemDto> findAll();

    CartItemDto update(int quantity, Long id);

    void delete(Long id);
}
