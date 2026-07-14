package com.app.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "managergroup")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ManagerGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "managerGroup")
    @JsonIgnore
    private List<User> users;

    @OneToMany(mappedBy = "managerGroup")
    @JsonIgnore
    private List<Room> rooms;
}
