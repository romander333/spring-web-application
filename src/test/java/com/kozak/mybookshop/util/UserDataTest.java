package com.kozak.mybookshop.util;

import static com.kozak.mybookshop.util.RoleDataTest.getRole;

import com.kozak.mybookshop.model.Role;
import com.kozak.mybookshop.model.User;
import java.util.Set;

public class UserDataTest {

    public static User sampleUser() {
        Role role = getRole();
        User user = new User();
        user.setId(1L);
        user.setEmail("romander@gmail.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("$2a$12$k5M6AyJ4itLQTb6KgNZsCeTROTmcRGE0AUr9Z0Kk/Mr9aDM8LPkYq");
        user.setShippingAddress("Some address");
        user.setRoles(Set.of(role));
        return user;
    }
}
