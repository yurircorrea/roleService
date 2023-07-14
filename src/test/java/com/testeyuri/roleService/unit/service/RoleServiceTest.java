package com.testeyuri.roleService.unit.service;

import com.testeyuri.roleService.entity.Membership;
import com.testeyuri.roleService.entity.Role;
import com.testeyuri.roleService.exception.BadRequestException;
import com.testeyuri.roleService.exception.NotFoundException;
import com.testeyuri.roleService.repository.MembershipRepository;
import com.testeyuri.roleService.repository.RoleRepository;
import com.testeyuri.roleService.service.RoleService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private MembershipRepository membershipRepository;

    @InjectMocks
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test()
    void testCreateRole_Success() {
        String roleName = "Developer";
        Role existingRole = new Role(UUID.randomUUID().toString(), roleName);
        when(roleRepository.findByName(roleName)).thenReturn(null);
        when(roleRepository.save(any(Role.class))).thenReturn(existingRole);

        Role createdRole = roleService.createRole(roleName);

        assertNotNull(createdRole);
        assertEquals(roleName, createdRole.getName());
        verify(roleRepository, times(1)).findByName(roleName);
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    void testCreateRole_RoleAlreadyExists() {
        String roleName = "Developer";
        Role existingRole = new Role(UUID.randomUUID().toString(), roleName);
        when(roleRepository.findByName(roleName)).thenReturn(existingRole);

        assertThrows(BadRequestException.class, () -> roleService.createRole(roleName));

        verify(roleRepository, times(1)).findByName(roleName);
        verify(roleRepository, times(0)).save(any(Role.class));
    }

}