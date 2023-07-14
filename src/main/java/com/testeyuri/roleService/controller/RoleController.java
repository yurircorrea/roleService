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
import com.testeyuri.roleService.exception.BadRequestException;
import com.testeyuri.roleService.exception.NotFoundException;
import com.testeyuri.roleService.service.RoleService;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/createRole")
    public ResponseEntity createRole(@RequestBody RoleRequest roleRequest){
        try{
            Role createdRole = roleService.createRole(roleRequest.getRoleName());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
        }catch(BadRequestException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{memberId}/teams/{teamId}")
    public ResponseEntity assignRole(
            @PathVariable String memberId,
            @PathVariable String teamId,
            @RequestBody RoleRequest roleRequest){

        try{
            Membership createdMembership = roleService.assignRole(memberId, teamId, roleRequest.getRoleName());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdMembership);
        }catch(BadRequestException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/{memberId}/teams/{teamId}")
    public ResponseEntity getRoleForMembership(
            @PathVariable String memberId,
            @PathVariable String teamId) {
        try {
            MembershipRole role = roleService.getRoleForMembership(memberId, teamId);
            return ResponseEntity.ok(role);
        }catch(BadRequestException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{roleName}/memberships")
    public ResponseEntity getMembershipsForRole(
            @PathVariable String roleName) {
        try {
            List<Membership> memberships = roleService.getMembershipsForRole(roleName);
            return ResponseEntity.ok(memberships);
        }catch(BadRequestException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }catch (NotFoundException e){
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}