# Roles and Memberships Web Service

This is a web service that enhances the Users and Teams services by introducing the concept of team roles and the ability to associate them with team members. It provides endpoints to create roles, assign roles to team members, lookup roles for memberships, and lookup memberships for a role.
This WEB service was developed as part of the coding challenge for e-Core.

## Table of Contents

- [Description of the Problem Approach and Solution](#approach)
- [Installation and Execution Instructions](#installation-and-execution)
- [Improvement Suggestion for Teams and Members Services](#evolution-suggestions)
- [Validation and Edge Cases Considered](#edge-cases-considered)

## Description of the Problem Approach and Solution

To solve the problem, the following approach was used:

1. **Identify the Requirements**: Understand the problem statement and the desired functionality of the Roles service.
2. **Creation of GIT Repository**: An new repository was created on GitHub so the solution code could be easily provided.
3. **Creation of the Database**: An MySQL database was created to store the data regarding the roles and memberships of members on the teams.
4. **Design the Data Model**: Define the necessary entities (Role, Membership) and their relationships. An Membership contains one Role, one User and one Team. However, Users, Teams and Roles can be included in as many Memberships as desired.
5. **Service Layer**: Creation of the RoleService class to handle business logic and interact with the database through the RoleRepository and MembershipRepository interfaces.
6. **Controller Layer**: Development of the RoleController class to define REST endpoints and handle incoming HTTP requests.
7. **Unit Tests**: Creation of unit tests using JUnit and Mockito to validate the functionality of the service. The most important tests were made over the RoleService class. However, some entity classes were also tested.
8. **API Integration Tests**: Development of API integration tests to ensure the correct behavior of the RoleController endpoints.

## Installation and Execution

There is a possibility to execute this project either with or without Docker.


## Improvement Suggestion for Teams and Members Services

In the current scenario, the Teams service is returning all the members of a team in a array named teamMemberIds, included on the Team object.
The enhancement made with the Roles and Memberships Service already allows this to be improved, since it provides a new Object to handle the participation of a member on a team, which is the Membership.
This also allows the developers of the Teams service to remove the teamMemberIds object from the returning JSON, once this information can now be acquired differently.

## Validation and Edge Cases Considered

The unit and API integration tests for the Roles Web Service cover several edge cases, including:

### 1. Role Creation
The role creation endpoint validates for the following:
- Creating a role that already exists.

### 2. Role Assignment
The role assignment endpoint validates for the following:
- Assigning a role to a team member who is already part of the team;
- Assigning a non-existent role to a team member
- Assigning a role for a non-existing member or team. For this validation, a request to the Teams and Members services is made, in order to obtain tha data and check if the member and the team both exists.

### 3. Role for the Membership Lookup
The role for membership lookup endpoint validates for the following:
- Looking up a role for a membership that does not exist.

### 3. Memberships for the Role Lookup
The membership for role lookup endpoint validates for the following:
- Looking up memberships for a role that does not exist.

### 4. Other Validations
- Handling malformed requests or invalid input parameters. 
- Verifying the correct HTTP status codes and response bodies for various scenarios.

## What happens if the data you are using gets deleted?
If the data in the Teams and Members services gets deleted, the Roles and Memberships service will prevent new data to be created using the deleted information, since it's already done in the validations.
For the already existing data, no action will be taken.

If any Role is deleted from the database. A cascade clause configured on the database will ensure the deletion of all the associated memberships.