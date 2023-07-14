package com.testeyuri.roleService.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testeyuri.roleService.controller.RoleController;
import com.testeyuri.roleService.entity.Membership;
import com.testeyuri.roleService.entity.MembershipRole;
import com.testeyuri.roleService.entity.Role;
import com.testeyuri.roleService.entity.RoleRequest;
import com.testeyuri.roleService.exception.BadRequestException;
import com.testeyuri.roleService.exception.NotFoundException;
import com.testeyuri.roleService.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class RoleControllerTest {

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateRole_Success() {
        String roleName = "Developer";
        RoleRequest roleRequest = new RoleRequest(roleName);
        Role createdRole = new Role(UUID.randomUUID().toString(), roleName);

        when(roleService.createRole(eq(roleName))).thenReturn(createdRole);

        ResponseEntity<Role> response = roleController.createRole(roleRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdRole, response.getBody());

        verify(roleService, times(1)).createRole(eq(roleName));
    }

    @Test
    void testCreateRole_BadRequest() {
        String roleName = "Developer";
        RoleRequest roleRequest = new RoleRequest(roleName);

        when(roleService.createRole(eq(roleName))).thenThrow(new BadRequestException("Bad request"));

        ResponseEntity<?> response = roleController.createRole(roleRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        verify(roleService, times(1)).createRole(eq(roleName));
    }

    @Test
    void testAssignRole_Success() throws Exception {
        String memberId = "fd282131-d8aa-4819-b0c8-d9e0bfb1b75c";
        String teamId = "7676a4bf-adfe-415c-941b-1739af07039b";
        String roleName = "Developer";
        RoleRequest roleRequest = new RoleRequest(roleName);
        Membership createdMembership = new Membership(UUID.randomUUID().toString(), memberId, teamId, UUID.randomUUID().toString());

        when(roleService.assignRole(eq(memberId), eq(teamId), eq(roleName))).thenReturn(createdMembership);

        ResponseEntity<Membership> response = roleController.assignRole(memberId, teamId, roleRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdMembership, response.getBody());

        verify(roleService, times(1)).assignRole(eq(memberId), eq(teamId), eq(roleName));
    }

    @Test
    void testAssignRole_BadRequest() throws Exception {
        String memberId = "fd282131-d8aa-4819-b0c8-d9e0bfb1b75c";
        String teamId = "7676a4bf-adfe-415c-941b-1739af07039b";
        String roleName = "Developer";
        RoleRequest roleRequest = new RoleRequest(roleName);

        when(roleService.assignRole(eq(memberId), eq(teamId), eq(roleName))).thenThrow(new BadRequestException("Bad request"));

        ResponseEntity<?> response = roleController.assignRole(memberId, teamId, roleRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        verify(roleService, times(1)).assignRole(eq(memberId), eq(teamId), eq(roleName));
    }

    @Test
    void testGetRoleForMembership_Success() {
        String memberId = "fd282131-d8aa-4819-b0c8-d9e0bfb1b75c";
        String teamId = "7676a4bf-adfe-415c-941b-1739af07039b";
        MembershipRole membershipRole = new MembershipRole(new Membership(), new Role());

        when(roleService.getRoleForMembership(eq(memberId), eq(teamId))).thenReturn(membershipRole);

        ResponseEntity<MembershipRole> response = roleController.getRoleForMembership(memberId, teamId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(membershipRole, response.getBody());

        verify(roleService, times(1)).getRoleForMembership(eq(memberId), eq(teamId));
    }

    @Test
    void testGetRoleForMembership_BadRequest() {
        String memberId = "fd282131-d8aa-4819-b0c8-d9e0bfb1b75c";
        String teamId = "7676a4bf-adfe-415c-941b-1739af07039b";

        when(roleService.getRoleForMembership(eq(memberId), eq(teamId))).thenThrow(new BadRequestException("Bad request"));

        ResponseEntity<?> response = roleController.getRoleForMembership(memberId, teamId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        verify(roleService, times(1)).getRoleForMembership(eq(memberId), eq(teamId));
    }

    @Test
    void testGetMembershipsForRole_Success() throws Exception {
        String roleName = "Developer";
        List<Membership> memberships = new ArrayList<>();
        memberships.add(new Membership());

        when(roleService.getMembershipsForRole(eq(roleName))).thenReturn(memberships);

        ResponseEntity<List<Membership>> response = roleController.getMembershipsForRole(roleName);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(memberships, response.getBody());

        verify(roleService, times(1)).getMembershipsForRole(eq(roleName));
    }

    @Test
    void testGetMembershipsForRole_BadRequest() throws Exception {
        String roleName = "Developer";

        when(roleService.getMembershipsForRole(eq(roleName))).thenThrow(new BadRequestException("Bad request"));

        ResponseEntity<?> response = roleController.getMembershipsForRole(roleName);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        verify(roleService, times(1)).getMembershipsForRole(eq(roleName));
    }

    @Test
    void testGetMembershipsForRole_NotFound() throws Exception {
        String roleName = "Developer";

        when(roleService.getMembershipsForRole(eq(roleName))).thenThrow(new NotFoundException("Not found"));

        ResponseEntity<?> response = roleController.getMembershipsForRole(roleName);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(roleService, times(1)).getMembershipsForRole(eq(roleName));
    }
}
