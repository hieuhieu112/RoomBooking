package com.app.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "manager_group")
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
    private List<User> users;

    @OneToMany(mappedBy = "managerGroup")
//    @JoinTable(
//            name = "manager_group_room",
//            joinColumns = @JoinColumn(name = "manager_group_id"),
//            inverseJoinColumns = @JoinColumn(name = "room_id")
//    )
    private List<Room> rooms;
}
