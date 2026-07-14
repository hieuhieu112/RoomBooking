package com.app.backend.entity;

import com.app.backend.entity.baseEntity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "roomtype")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomType extends BaseEntity {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @OneToMany
    @JoinColumn(name = "roomType")
    @JsonIgnore
    private List<Room> rooms;
}
