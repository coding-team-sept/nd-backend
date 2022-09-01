package com.github.coding_team_sept.nd_backend.authentication.components;

import com.github.coding_team_sept.nd_backend.authentication.enums.RoleType;
import com.github.coding_team_sept.nd_backend.authentication.models.AppUser;
import com.github.coding_team_sept.nd_backend.authentication.models.Role;
import com.github.coding_team_sept.nd_backend.authentication.repositories.AuthenticationRepository;
import com.github.coding_team_sept.nd_backend.authentication.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private boolean alreadySetup = false;
    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private AuthenticationRepository authenticationRepo;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        Arrays.stream(RoleType.values()).forEach(this::createRole);
        createAdmin();
        alreadySetup = true;
    }

    @Transactional
    void createRole(RoleType roleType) {
        final var role = roleRepo.findRoleByRole(roleType);
        if (role.isEmpty()) {
            final var newRole = Role.builder().role(roleType).build();
            roleRepo.save(newRole);
        }
    }

    @Transactional
    void createAdmin() {
        final var email = "admin@admin.com";
        final var appUser = authenticationRepo.findUserByEmail(email);
        if (appUser.isEmpty()) {
            final var newAppUser = AppUser
                    .builder()
                    .name("Admin")
                    .email(email)
                    .password("admin")
                    .role(roleRepo.findRoleByRole(RoleType.ADMIN).orElse(null))
                    .build();
            authenticationRepo.save(newAppUser);
        }
    }
}
