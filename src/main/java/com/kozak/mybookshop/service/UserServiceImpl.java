package com.kozak.mybookshop.service;

import com.kozak.mybookshop.dto.user.UserRegistrationRequestDto;
import com.kozak.mybookshop.dto.user.UserResponseDto;
import com.kozak.mybookshop.exception.RegistrationException;
import com.kozak.mybookshop.mapper.UserMapper;
import com.kozak.mybookshop.model.User;
import com.kozak.mybookshop.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto registration(UserRegistrationRequestDto userRegistrationRequestDto) {
        if (userRepository.findByEmail(userRegistrationRequestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Email already registered");
        }
        User user = new User();
        user.setEmail(userRegistrationRequestDto.getEmail());
        user.setPassword(userRegistrationRequestDto.getPassword());
        user.setFirstName(userRegistrationRequestDto.getFirstName());
        user.setLastName(userRegistrationRequestDto.getLastName());
        user.setShippingAddress(userRegistrationRequestDto.getShippingAddress());
        User savedUser = userRepository.save(user);
        return userMapper.toUserResponseDto(savedUser);
    }
}
