package xyz.anycourse.app.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import xyz.anycourse.app.domain.enumeration.UserRole;

@Table(name = "roles")
@Entity
public class Role extends Base {
    @Enumerated
    private UserRole role;

    public Role() {
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
