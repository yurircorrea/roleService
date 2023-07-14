package com.testeyuri.roleService.unit.service;

import com.testeyuri.roleService.entity.Membership;
import com.testeyuri.roleService.entity.MembershipRole;
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

    @Test
    void testAssignRole_Success() throws IOException {
        String memberId = "fd282131-d8aa-4819-b0c8-d9e0bfb1b75c";
        String teamId = "7676a4bf-adfe-415c-941b-1739af07039b";
        String roleName = "Developer";

        String teamsPath = "https://cgjresszgg.execute-api.eu-west-1.amazonaws.com/teams/";
        String membersPath = "https://cgjresszgg.execute-api.eu-west-1.amazonaws.com/users/";

        roleService.setTeamsPath(teamsPath);
        roleService.setMembersPath(membersPath);

        Role role = new Role(UUID.randomUUID().toString(), roleName);
        when(roleRepository.findByName(roleName)).thenReturn(role);
        when(membershipRepository.findByMemberAndTeam(memberId, teamId)).thenReturn(null);

        Membership savedMembership = new Membership(UUID.randomUUID().toString(), memberId, teamId, role.getId());
        when(membershipRepository.save(any(Membership.class))).thenReturn(savedMembership);

        Membership assignedMembership = roleService.assignRole(memberId, teamId, roleName);

        assertNotNull(assignedMembership);
        assertEquals(memberId, assignedMembership.getMember());
        assertEquals(teamId, assignedMembership.getTeam());
        assertEquals(role.getId(), assignedMembership.getRole());
        verify(roleRepository, times(1)).findByName(roleName);
        verify(membershipRepository, times(1)).findByMemberAndTeam(memberId, teamId);
        verify(membershipRepository, times(1)).save(any(Membership.class));
    }

    @Test
    void testAssignRole_RoleNotFound() throws IOException {
        String memberId = "fd282131-d8aa-4819-b0c8-d9e0bfb1b75c";
        String teamId = "7676a4bf-adfe-415c-941b-1739af07039b";
        String roleName = "NonExistentRole";

        String teamsPath = "https://cgjresszgg.execute-api.eu-west-1.amazonaws.com/teams/";
        String membersPath = "https://cgjresszgg.execute-api.eu-west-1.amazonaws.com/users/";

        roleService.setTeamsPath(teamsPath);
        roleService.setMembersPath(membersPath);

        when(roleRepository.findByName(roleName)).thenReturn(null);

        assertThrows(BadRequestException.class, () -> roleService.assignRole(memberId, teamId, roleName));

        verify(roleRepository, times(1)).findByName(roleName);
        verify(membershipRepository, times(0)).findByMemberAndTeam(anyString(), anyString());
        verify(membershipRepository, times(0)).save(any(Membership.class));
    }

    @Test
    void testAssignRole_MemberAlreadyOnTeam() throws IOException {
        String memberId = "fd282131-d8aa-4819-b0c8-d9e0bfb1b75c";
        String teamId = "7676a4bf-adfe-415c-941b-1739af07039b";
        String roleName = "Developer";

        String teamsPath = "https://cgjresszgg.execute-api.eu-west-1.amazonaws.com/teams/";
        String membersPath = "https://cgjresszgg.execute-api.eu-west-1.amazonaws.com/users/";

        roleService.setTeamsPath(teamsPath);
        roleService.setMembersPath(membersPath);

        Role role = new Role(UUID.randomUUID().toString(), roleName);
        when(roleRepository.findByName(roleName)).thenReturn(role);
        when(membershipRepository.findByMemberAndTeam(memberId, teamId)).thenReturn(new Membership());

        assertThrows(BadRequestException.class, () -> roleService.assignRole(memberId, teamId, roleName));

        verify(roleRepository, times(1)).findByName(roleName);
        verify(membershipRepository, times(1)).findByMemberAndTeam(memberId, teamId);
        verify(membershipRepository, times(0)).save(any(Membership.class));
    }

    @Test
    void testAssignRole_MemberNotFound() throws IOException {
        String memberId = "NonExistentMember";
        String teamId = "7676a4bf-adfe-415c-941b-1739af07039b";
        String roleName = "Developer";

        String teamsPath = "https://cgjresszgg.execute-api.eu-west-1.amazonaws.com/teams/";
        String membersPath = "https://cgjresszgg.execute-api.eu-west-1.amazonaws.com/users/";

        roleService.setTeamsPath(teamsPath);
        roleService.setMembersPath(membersPath);

        assertThrows(BadRequestException.class, () -> roleService.assignRole(memberId, teamId, roleName));

        verify(membershipRepository, times(0)).save(any(Membership.class));
    }

    @Test
    void testAssignRole_TeamNotFound() throws IOException {
        String memberId = "fd282131-d8aa-4819-b0c8-d9e0bfb1b75c";
        String teamId = "NonExistentTeam";
        String roleName = "Developer";

        String teamsPath = "https://cgjresszgg.execute-api.eu-west-1.amazonaws.com/teams/";
        String membersPath = "https://cgjresszgg.execute-api.eu-west-1.amazonaws.com/users/";

        roleService.setTeamsPath(teamsPath);
        roleService.setMembersPath(membersPath);

        assertThrows(BadRequestException.class, () -> roleService.assignRole(memberId, teamId, roleName));

        verify(membershipRepository, times(0)).save(any(Membership.class));
    }

    @Test
    void testGetRoleForMembership_Success() {
        String memberId = "fd282131-d8aa-4819-b0c8-d9e0bfb1b75c";
        String teamId = "7676a4bf-adfe-415c-941b-1739af07039b";
        String roleName = "Developer";

        Role role = new Role(UUID.randomUUID().toString(), roleName);
        Membership membership = new Membership(UUID.randomUUID().toString(), memberId, teamId, role.getId());

        when(membershipRepository.findByMemberAndTeam(memberId, teamId)).thenReturn(membership);
        when(roleRepository.findById(role.getId())).thenReturn(role);

        MembershipRole membershipRole = roleService.getRoleForMembership(memberId, teamId);

        assertNotNull(membershipRole);
        assertEquals(membership, membershipRole.getMembership());
        assertEquals(role, membershipRole.getRole());
        verify(membershipRepository, times(1)).findByMemberAndTeam(memberId, teamId);
        verify(roleRepository, times(1)).findById(role.getId());
    }

    @Test
    void testGetRoleForMembership_MembershipNotFound() {
        String memberId = "fd282131-d8aa-4819-b0c8-d9e0bfb1b75c";
        String teamId = "7676a4bf-adfe-415c-941b-1739af07039b";

        when(membershipRepository.findByMemberAndTeam(memberId, teamId)).thenReturn(null);

        assertThrows(BadRequestException.class, () -> roleService.getRoleForMembership(memberId, teamId));

        verify(membershipRepository, times(1)).findByMemberAndTeam(memberId, teamId);
        verify(roleRepository, times(0)).findById(anyString());
    }

    @Test
    void testGetMembershipsForRole_Success() {
        String roleName = "Developer";

        Role role = new Role(UUID.randomUUID().toString(), roleName);
        List<Membership> memberships = new ArrayList<>();
        memberships.add(new Membership());

        when(roleRepository.findByName(roleName)).thenReturn(role);
        when(membershipRepository.findAllByRole(role.getId())).thenReturn(memberships);

        List<Membership> foundMemberships = roleService.getMembershipsForRole(roleName);

        assertNotNull(foundMemberships);
        assertEquals(memberships.size(), foundMemberships.size());
        verify(roleRepository, times(1)).findByName(roleName);
        verify(membershipRepository, times(1)).findAllByRole(role.getId());
    }

    @Test
    void testGetMembershipsForRole_RoleNotFound() {
        String roleName = "NonExistentRole";

        when(roleRepository.findByName(roleName)).thenReturn(null);

        assertThrows(BadRequestException.class, () -> roleService.getMembershipsForRole(roleName));

        verify(roleRepository, times(1)).findByName(roleName);
        verify(membershipRepository, times(0)).findAllByRole(anyString());
    }

    @Test
    void testGetMembershipsForRole_NoMembershipsFound() {
        String roleName = "Developer";

        Role role = new Role(UUID.randomUUID().toString(), roleName);

        when(roleRepository.findByName(roleName)).thenReturn(role);
        when(membershipRepository.findAllByRole(role.getId())).thenReturn(new ArrayList<>());

        assertThrows(NotFoundException.class, () -> roleService.getMembershipsForRole(roleName));

        verify(roleRepository, times(1)).findByName(roleName);
        verify(membershipRepository, times(1)).findAllByRole(role.getId());
    }

}