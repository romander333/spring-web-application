package com.kozak.mybookshop.controller;

import com.kozak.mybookshop.dto.cartitem.CreateCartItemRequestDto;
import com.kozak.mybookshop.dto.shoppingcart.ShoppingCartDto;
import com.kozak.mybookshop.service.cartitem.CartItemService;
import com.kozak.mybookshop.service.shoppingcart.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartItemController {
    private final CartItemService cartItemService;
    private final ShoppingCartService shoppingCartService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Add new item", description = "Add new item to the cart")
    public ShoppingCartDto addCartItem(@RequestBody @Valid CreateCartItemRequestDto requestDto) {
        return shoppingCartService.addCartItem(requestDto);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get shopping cart", description = "Get shopping cart")
    public ShoppingCartDto getShoppingCart() {
        return shoppingCartService.getShoppingCart();
    }

    @PutMapping("/items/{id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Update certain item", description = "Update item in the cart by id")
    public void updateCartItemQuantity(
            @PathVariable Long id,
            @RequestParam @Min(1) int quantity) {
        cartItemService.update(quantity, id);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Delete item", description = "Delete item from cart by id")
    public void deleteCartItem(@PathVariable Long id) {
        cartItemService.delete(id);
    }
}
