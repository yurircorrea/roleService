package com.testeyuri.roleService.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Component
@Table(name = "roles")
public class Role {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;
}