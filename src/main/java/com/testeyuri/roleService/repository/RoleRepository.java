package com.testeyuri.roleService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.testeyuri.roleService.entity.Role;

@Repository
@Component
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}