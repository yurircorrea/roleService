package com.testeyuri.roleService.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.testeyuri.roleService.entity.*;

import com.testeyuri.roleService.helper.GetObjectFromURL;
import com.testeyuri.roleService.repository.MembershipRepository;
import com.testeyuri.roleService.repository.RoleRepository;

@Service
@Component
@Transactional
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MembershipRepository membershipRepository;

    @Value("${teams.path}")
    private String teamsPath;

    @Value("${members.path}")
    private String membersPath;

    public Role createRole(String name) {
        //Verifying if the given Role already exists
        if (roleRepository.findByName(name) != null) {
            throw new IllegalArgumentException("Role already exists");
        }

        //Saving the new Role
        Role role = new Role(UUID.randomUUID().toString(), name);
        return roleRepository.save(role);
    }

    public Membership assignRole(String memberId, String teamId, String roleName) throws IOException {
        //Verifying if the given Member is already allocated in the given team
        if (membershipRepository.findByMemberAndTeam(memberId, teamId) != null){
            throw new IllegalArgumentException("The specified member is already on the Team");
        }

        //Verifying if the given Role exists in the database
        Role role = roleRepository.findByName(roleName);
        if (role == null){
            throw new IllegalArgumentException("The specified Role was not found");
        }

        //Accessing the Teams and Members services to check if both of them exists to ensure data integrity
        JsonNode json = GetObjectFromURL.get(new URL(teamsPath + teamId));
        if(json.get("id") == null){
            throw new IllegalArgumentException("The specified Team was not found!");
        }
        System.out.println("INFO: Team Name: " + json.get("name"));

        json = GetObjectFromURL.get(new URL(membersPath + memberId));
        if(json.get("id") == null){
            throw new IllegalArgumentException("The specified Member was not found!");
        }
        System.out.println("INFO: Member Name: " + json.get("firstName"));

        //If all the validations pass, saving the new Membership
        Membership membership = new Membership(UUID.randomUUID().toString(), memberId, teamId, role.getId());
        return  membershipRepository.save(membership);
    }

    public MembershipRole getRoleForMembership(String memberId, String teamId) throws IOException {
        //There is no need to check either the Member or Team exist or not, because there will be no data storing. In case
        // any of them doesn't exist, the Membership won't be found and an Exception will already be thrown.

        Membership membership = membershipRepository.findByMemberAndTeam(memberId, teamId);
        if(membership == null){
            throw new IllegalArgumentException("No Membership was found for the given Member and Team");
        }

        Role role = roleRepository.findById(membership.getRole());

        return new MembershipRole(membership, role);
    }

    public List<Membership> lookupMembershipsForRole(String roleName) {

        Membership m = new Membership(UUID.randomUUID().toString(), "member", "team" ,"role");
        List<Membership> list = new ArrayList<Membership>();

        list.add(m);
        return list;
        // Implementation logic to lookup memberships for a role
    }
}