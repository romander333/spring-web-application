package com.kozak.mybookshop.repository.shoppingcart;

import com.kozak.mybookshop.model.ShoppingCart;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShoppingCartRepositoryTest {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;


    @Test
    void findShoppingCartByUserId_WithValidUserId_ShouldReturnShoppingCart() {
        Long userId = 1L;
        Optional<ShoppingCart> shoppingCartDto = shoppingCartRepository.findShoppingCartByUser_Id(userId);
        Assertions.assertTrue(shoppingCartDto.isPresent());
        Assertions.assertEquals(userId, shoppingCartDto.get().getUser().getId());
    }
}
