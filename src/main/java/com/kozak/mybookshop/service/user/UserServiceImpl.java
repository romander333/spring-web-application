package com.kozak.mybookshop.service.user;

import com.kozak.mybookshop.dto.user.UserRegistrationRequestDto;
import com.kozak.mybookshop.dto.user.UserResponseDto;
import com.kozak.mybookshop.exception.EntityNotFoundException;
import com.kozak.mybookshop.exception.RegistrationException;
import com.kozak.mybookshop.mapper.UserMapper;
import com.kozak.mybookshop.model.Role;
import com.kozak.mybookshop.model.ShoppingCart;
import com.kozak.mybookshop.model.User;
import com.kozak.mybookshop.repository.role.RoleRepository;
import com.kozak.mybookshop.repository.shoppingcart.ShoppingCartRepository;
import com.kozak.mybookshop.repository.user.UserRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public UserResponseDto registration(UserRegistrationRequestDto userRegistrationRequestDto) {
        if (userRepository.existsByEmail(userRegistrationRequestDto.getEmail())) {
            throw new RegistrationException("This email: "
                    + userRegistrationRequestDto.getEmail()
                    + " is already registered");
        }
        User user = userMapper.toModel(userRegistrationRequestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepository.findByName(Role.RoleName.USER)
                .orElseThrow(() -> new EntityNotFoundException("Role USER not found"));
        user.setRoles(Set.of(role));
        userRepository.save(user);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
        return userMapper.toUserResponseDto(user);
    }
}
