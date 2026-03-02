package com.onehealth.kernel.auth;

import org.springframework.security.core.GrantedAuthority;

public record RbacAuthority(Role role, String module, String permission) implements GrantedAuthority {

    @Override
    public String getAuthority() {
        return "ROLE_" + role + "_" + module + "_" + permission;
    }
}
