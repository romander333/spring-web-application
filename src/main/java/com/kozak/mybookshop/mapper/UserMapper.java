package com.kozak.mybookshop.mapper;

import com.kozak.mybookshop.config.MapperConfig;
import com.kozak.mybookshop.dto.user.UserRegistrationRequestDto;
import com.kozak.mybookshop.dto.user.UserResponseDto;
import com.kozak.mybookshop.model.Role;
import com.kozak.mybookshop.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toUserResponseDto(User user);

    @Named("mapRoleNameToRole")
    default Role mapRoleNameToRole(Role.RoleName roleName) {
        return new Role(roleName);
    }

    @Mapping(source = "roles", target = "roles", qualifiedByName = "mapRoleNameToRole")
    User toModel(UserRegistrationRequestDto userRegistrationRequestDto);
}
