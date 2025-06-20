package com.kozak.mybookshop.service.cartitem;

import com.kozak.mybookshop.dto.cartItem.CartItemDto;
import com.kozak.mybookshop.dto.cartItem.CreateCartItemRequestDto;
import com.kozak.mybookshop.exception.EntityNotFoundException;
import com.kozak.mybookshop.mapper.CartItemMapper;
import com.kozak.mybookshop.model.CartItem;
import com.kozak.mybookshop.repository.cartitem.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;

    @Override
    public CartItemDto save(CreateCartItemRequestDto requestDto) {
        CartItem cartItem = cartItemMapper.toModel(requestDto);
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public List<CartItemDto> findAll() {
        return cartItemRepository.findAll().stream()
                .map(cartItemMapper::toDto)
                .toList();
    }

    @Override
    public CartItemDto update(int quantity, Long id) {
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("CartItem with id " + id + " not found")
        );
        return cartItemMapper.toDto(cartItemRepository.updateQuantityById(quantity, id));
    }

    @Override
    public void delete(Long id) {
        cartItemRepository.deleteById(id);
    }
}
