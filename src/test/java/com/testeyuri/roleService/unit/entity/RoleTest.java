package com.testeyuri.roleService.unit.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import com.testeyuri.roleService.entity.Role;

class RoleTest {

    @Test
    void testRoleModel() {
        Role role = new Role(UUID.randomUUID().toString(), "Developer");

        assertNotNull(role.getId());
        assertEquals("Developer", role.getName());

        role.setName("Tester");
        role.setId(UUID.randomUUID().toString());

        assertNotNull(role.getId());
        assertEquals("Tester", role.getName());
    }
}






