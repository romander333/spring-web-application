package com.kozak.mybookshop.controller;

import com.kozak.mybookshop.dto.cartItem.CartItemDto;
import com.kozak.mybookshop.dto.cartItem.CreateCartItemRequestDto;
import com.kozak.mybookshop.dto.shoppingcart.ShoppingCartDto;
import com.kozak.mybookshop.service.cartitem.CartItemService;
import java.util.List;

import com.kozak.mybookshop.service.shoppingcart.ShoppingCartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartItemController {
    private final CartItemService cartItemService;
    private final ShoppingCartService shoppingCartService;

    @PostMapping
    public ShoppingCartDto addCartItem(@RequestBody @Valid CreateCartItemRequestDto requestDto) {
        return shoppingCartService.addCartItem(requestDto);
    }

    @GetMapping
    public ShoppingCartDto getShoppingCart() {
        return shoppingCartService.getShoppingCart();
    }

    @PutMapping("/items/{id}")
    public CartItemDto updateCartItemQuantity
            (
            @PathVariable Long id,
            @RequestParam @Min(1) int quantity) {
        return cartItemService.update(quantity, id);
    }

    @DeleteMapping("{id}")
    public void deleteCartItem(@PathVariable Long id) {
        cartItemService.delete(id);
    }
}
