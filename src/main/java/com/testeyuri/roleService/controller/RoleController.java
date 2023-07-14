package com.testeyuri.roleService.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.testeyuri.roleService.entity.Membership;
import com.testeyuri.roleService.entity.MembershipRole;
import com.testeyuri.roleService.entity.Role;
import com.testeyuri.roleService.entity.RoleRequest;
import com.testeyuri.roleService.service.RoleService;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/createRole")
    public ResponseEntity<Role> createRole(@RequestBody RoleRequest roleRequest) {
        Role createdRole = roleService.createRole(roleRequest.getRoleName());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
    }

    @PostMapping("/{memberId}/teams/{teamId}")
    public ResponseEntity<Membership> assignRole(
            @PathVariable String memberId,
            @PathVariable String teamId,
            @RequestBody RoleRequest roleRequest) throws IOException {
        Membership createdMembership = roleService.assignRole(memberId, teamId, roleRequest.getRoleName());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMembership);
    }

    @GetMapping("/{memberId}/teams/{teamId}")
    public ResponseEntity<MembershipRole> getRoleForMembership(
            @PathVariable String memberId,
            @PathVariable String teamId) throws IOException {
        MembershipRole role = roleService.getRoleForMembership(memberId, teamId);
        return ResponseEntity.ok(role);
    }

    @GetMapping("/{roleName}/memberships")
    public ResponseEntity<List<Membership>> getMembershipsForRole(
            @PathVariable String roleName) {
        List<Membership> memberships = roleService.lookupMembershipsForRole(roleName);
        return ResponseEntity.ok(memberships);
    }
}