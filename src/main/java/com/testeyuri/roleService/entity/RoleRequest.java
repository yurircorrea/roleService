package com.testeyuri.roleService.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RoleRequest {
    private String roleName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}