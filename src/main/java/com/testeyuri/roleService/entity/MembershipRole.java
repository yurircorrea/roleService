package com.testeyuri.roleService.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MembershipRole {
    private Membership membership;
    private Role role;
}
