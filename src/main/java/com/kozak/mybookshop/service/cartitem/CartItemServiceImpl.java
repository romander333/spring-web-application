package com.kozak.mybookshop.service.cartitem;

import com.kozak.mybookshop.repository.cartitem.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;

    @Override
    public void delete(Long id) {
        cartItemRepository.deleteById(id);
    }
}
