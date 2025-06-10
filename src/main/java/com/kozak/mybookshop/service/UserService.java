package com.kozak.mybookshop.service;

import com.kozak.mybookshop.dto.user.UserRegistrationRequestDto;
import com.kozak.mybookshop.dto.user.UserResponseDto;

public interface UserService {

    UserResponseDto registration(UserRegistrationRequestDto userRegistrationRequestDto);
}
