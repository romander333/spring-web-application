package com.kozak.mybookshop.util;

import com.kozak.mybookshop.model.Role;

public class RoleDataTest {

    public static Role getRole() {
        Role role = new Role();
        role.setId(1L);
        role.setName(Role.RoleName.USER);
        return role;
    }
}
