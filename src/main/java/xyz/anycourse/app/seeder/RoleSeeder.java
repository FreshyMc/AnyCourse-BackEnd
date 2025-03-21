package xyz.anycourse.app.seeder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import xyz.anycourse.app.domain.entity.Role;
import xyz.anycourse.app.domain.enumeration.UserRole;
import xyz.anycourse.app.repository.RoleRepository;

@Component
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        seedRoles();
    }

    private void seedRoles() {
        if (roleRepository.count() > 0) {
            return;
        }

        for (UserRole userRole : UserRole.values()) {
            Role role = new Role();
            role.setRole(userRole);

            roleRepository.save(role);
        }
    }
}
