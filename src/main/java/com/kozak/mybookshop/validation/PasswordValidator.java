package com.kozak.mybookshop.validation;

import com.kozak.mybookshop.dto.user.UserRegistrationRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, Object> {
    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        if (object instanceof UserRegistrationRequestDto requestDto) {
            return requestDto.getPassword().equals(requestDto.getRepeatPassword());
        }
        return false;
    }
}
