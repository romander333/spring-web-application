package com.kozak.mybookshop.util;

import com.kozak.mybookshop.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public class UserDataTest {

    public static User sampleUser(Long userId) {
        User user = new User();
        user.setId(userId);
        user.setEmail("romander@gmail.com");
        user.setFirstName("Roman");
        return user;
    }

    public static Authentication sampleAuthentication(User user) {
        return new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of(new SimpleGrantedAuthority("ROLE_USER"));
            }

            @Override
            public Object getCredentials() {
                return user.getPassword();
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return user; // ← важливо, щоб твій об'єкт User повертався
            }

            @Override
            public boolean isAuthenticated() {
                return true;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {}

            @Override
            public String getName() {
                return user.getEmail();
            }
        };
    }
}
