package com.app.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "house")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "house")
    @JsonIgnore
    private List<Room> room;
}
