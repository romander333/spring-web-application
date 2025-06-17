package com.kozak.mybookshop.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserLoginRequestDto {
    @NotEmpty
    @Email
    @Length(min = 12, max = 30)
    private String email;
    @NotEmpty
    @Length(min = 8, max = 30)
    private String password;
}
