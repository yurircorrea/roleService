package com.testeyuri.roleService.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.databind.JsonNode;
import com.testeyuri.roleService.entity.*;
import com.testeyuri.roleService.exception.BadRequestException;
import com.testeyuri.roleService.exception.NotFoundException;
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

    public Role createRole(String name){
        //Verifying if the given Role already exists
        if (roleRepository.findByName(name) != null) {
            throw new BadRequestException("Role already exists");
        }

        //Saving the new Role
        Role role = new Role(UUID.randomUUID().toString(), name);
        return roleRepository.save(role);
    }

    public Membership assignRole(String memberId, String teamId, String roleName) throws IOException {

        //Accessing the Teams and Members services to check if both of them exists to ensure data integrity
        JsonNode team = GetObjectFromURL.get(new URL(teamsPath + teamId));
        JsonNode member = GetObjectFromURL.get(new URL(membersPath + memberId));
        if (team.get("id") == null || member.get("id") == null) {
            throw new BadRequestException("The specified Team Or Member doesn't exist");
        }

        //Show the team and member for logging purposes
        System.out.println("INFO: Team Name: " + team.get("name"));
        System.out.println("INFO: Member Name: " + member.get("firstName"));

        //Verifying if the given Role exists in the database
        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            throw new BadRequestException("The specified Role was not found");
        }

        //Verifying if the given Member is already allocated in the given team
        if (membershipRepository.findByMemberAndTeam(memberId, teamId) != null) {
            throw new BadRequestException("The specified member is already on the Team");
        }

        //If all the validations pass, saving the new Membership
        Membership membership = new Membership(UUID.randomUUID().toString(), memberId, teamId, role.getId());
        return membershipRepository.save(membership);
    }

    public MembershipRole getRoleForMembership(String memberId, String teamId){
        //There is no need to check either the Member or Team exist or not, because there will be no data storing. In case
        // any of them doesn't exist, the Membership won't be found and an Exception will already be thrown.

        //Verifying if the given Team and Member are part on a Membership
        Membership membership = membershipRepository.findByMemberAndTeam(memberId, teamId);
        if(membership == null){
            throw new BadRequestException("No Membership was found for the specified Team and Member");
        }

        //Finding the data about the Role - No validation is needed, since the previous validation guarantees the existence of a Role
        Role role = roleRepository.findById(membership.getRole());

        //Return a custom Bean, containing both the Membership and Role data
        return new MembershipRole(membership, role);
    }

    public List<Membership> getMembershipsForRole(String roleName) throws BadRequestException {

        //Validating the given Role name
        Role role = roleRepository.findByName(roleName);
        if(role == null){
            throw new BadRequestException("The specified Role does not exist");
        }

        //If the Role exists, find all the Memberships associated to it
        List<Membership> list = membershipRepository.findAllByRole(role.getId());

        if(list.isEmpty()){
            throw new NotFoundException("No Memberships were found for the given Role");
        }

        return list;
    }
}