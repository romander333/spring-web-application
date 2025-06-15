package com.kozak.mybookshop.dto.user;

import com.kozak.mybookshop.model.Role;
import com.kozak.mybookshop.validation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Password
public class UserRegistrationRequestDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Length(min = 8, max = 100)
    private String password;
    @NotBlank
    @Length(min = 8, max = 100)
    private String repeatPassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String shippingAddress;
    private Set<Role.RoleName> roles = new HashSet<>();
}
