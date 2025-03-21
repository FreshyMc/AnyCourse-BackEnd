package xyz.anycourse.app.domain.enumeration;

import xyz.anycourse.app.exception.UnknownRoleException;

import java.util.Arrays;

public enum UserRole {
    USER("ROLE_USER"), SELLER("ROLE_SELLER"), ADMIN("ROLE_ADMIN");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRoleName() {
        return this.role;
    }

    public UserRole getRoleByName(String roleName) {
        return Arrays.stream(UserRole.values())
                .filter(userRole -> userRole.role.equals(roleName))
                .findFirst()
                .orElseThrow(() -> new UnknownRoleException("Unknown user role: " + roleName));
    }
}
