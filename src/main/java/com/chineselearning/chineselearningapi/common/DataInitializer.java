package com.chineselearning.chineselearningapi.common;

import com.chineselearning.chineselearningapi.user.entity.Role;
import com.chineselearning.chineselearningapi.user.entity.RoleName;
import com.chineselearning.chineselearningapi.user.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {

        createRole(RoleName.ROLE_USER);
        createRole(RoleName.ROLE_CONTENT_ADMIN);
        createRole(RoleName.ROLE_SUPER_ADMIN);
    }

    private void createRole(RoleName roleName) {

        if (roleRepository.findByName(roleName).isEmpty()) {

            Role role = Role.builder()
                    .name(roleName)
                    .build();

            roleRepository.save(role);
        }
    }
}