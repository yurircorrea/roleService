package com.testeyuri.roleService.unit.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import com.testeyuri.roleService.entity.Membership;

class MembershipTest {

    @Test
    void testMembershipModel() {
        Membership membership = new Membership(UUID.randomUUID().toString(), "memberId", "teamId", "roleName");

        assertNotNull(membership.getId());
        assertNotNull(membership.getMember());
        assertNotNull(membership.getTeam());
        assertNotNull(membership.getRole());

        membership.setId(UUID.randomUUID().toString());
        membership.setMember("memberId2");
        membership.setTeam("teamId2");
        membership.setRole("roleName2");

        assertNotNull(membership.getId());
        assertEquals("memberId2", membership.getMember());
        assertEquals("teamId2", membership.getTeam());
        assertEquals("roleName2", membership.getRole());
    }
}






