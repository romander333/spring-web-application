package com.kozak.mybookshop.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class CoverImageValidator implements ConstraintValidator<CoverImage, String> {
    private static final String IMAGE_PATTERN =
            "([^\\s]+(\\.(?i)(/bmp|jpg|gif|png))$)";

    @Override
    public boolean isValid(String coverImage,
                           ConstraintValidatorContext constraintValidatorContext) {
        return coverImage != null && Pattern.compile(IMAGE_PATTERN)
                .matcher(coverImage)
                .matches();
    }
}
