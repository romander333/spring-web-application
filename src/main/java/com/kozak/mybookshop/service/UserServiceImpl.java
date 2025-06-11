package com.kozak.mybookshop.service;

import com.kozak.mybookshop.dto.user.UserRegistrationRequestDto;
import com.kozak.mybookshop.dto.user.UserResponseDto;
import com.kozak.mybookshop.exception.RegistrationException;
import com.kozak.mybookshop.mapper.UserMapper;
import com.kozak.mybookshop.model.User;
import com.kozak.mybookshop.repository.user.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto registration(UserRegistrationRequestDto userRegistrationRequestDto) {
        Optional<User> existingUser = userRepository.findByEmail(
                userRegistrationRequestDto.getEmail());
        if (existingUser.isPresent()) {
            throw new RegistrationException("Email already registered");
        }
        User user = userMapper.toModel(userRegistrationRequestDto);
        userRepository.save(user);
        return userMapper.toUserResponseDto(user);
    }
}
