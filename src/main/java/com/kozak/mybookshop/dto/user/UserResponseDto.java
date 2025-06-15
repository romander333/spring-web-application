package com.kozak.mybookshop.dto.user;

import com.kozak.mybookshop.model.Role;
import java.util.Set;

public record UserResponseDto(
        Long id,
        String email,
        String firstName,
        String lastName,
        String shippingAddress,
        Set<Role> roles) {
}
