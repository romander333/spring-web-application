package com.kozak.mybookshop.repository.shoppingcart;

import static com.kozak.mybookshop.util.ShoppingCartDataTest.sampleShoppingCart;
import static com.kozak.mybookshop.util.ShoppingCartDataTest.sampleShoppingCartDto;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.kozak.mybookshop.dto.shoppingcart.ShoppingCartDto;
import com.kozak.mybookshop.model.ShoppingCart;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShoppingCartRepositoryTest {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    void findShoppingCartByUserId_WithValidUserId_ShouldReturnShoppingCart() {
        Long userId = 1L;
        ShoppingCart expected = sampleShoppingCart();
        Optional<ShoppingCart> actual = shoppingCartRepository.findShoppingCartByUser_Id(userId);
        assertEquals(expected, actual.get());
    }
}
