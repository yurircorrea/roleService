package com.testeyuri.roleService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.testeyuri.roleService.entity.Membership;

@Repository
@Component
public interface MembershipRepository extends JpaRepository<Membership, Long> {
    Membership findByMemberAndTeam(String memberId, String teamId);
}