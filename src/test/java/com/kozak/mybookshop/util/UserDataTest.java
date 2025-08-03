package com.kozak.mybookshop.util;

import com.kozak.mybookshop.model.Role;
import com.kozak.mybookshop.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.kozak.mybookshop.util.RoleDataTest.getRole;

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
