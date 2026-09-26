package com.chineselearning.chineselearningapi.user.repository;

import com.chineselearning.chineselearningapi.user.entity.Role;
import com.chineselearning.chineselearningapi.user.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName name);
}