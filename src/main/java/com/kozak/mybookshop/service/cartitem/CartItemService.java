package com.kozak.mybookshop.service.cartitem;

public interface CartItemService {
    void update(int quantity, Long id);

    void delete(Long id);
}
