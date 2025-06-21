package com.kozak.mybookshop.service.cartitem;

import com.kozak.mybookshop.exception.EntityNotFoundException;
import com.kozak.mybookshop.mapper.CartItemMapper;
import com.kozak.mybookshop.model.CartItem;
import com.kozak.mybookshop.repository.cartitem.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;

    @Transactional
    @Override
    public void update(int quantity, Long id) {
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("CartItem with id " + id + " not found")
        );
        cartItemRepository.updateQuantityById(quantity, id);
    }

    @Override
    public void delete(Long id) {
        cartItemRepository.deleteById(id);
    }
}
