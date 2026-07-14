package com.app.backend.entity;

import com.app.backend.entity.baseEntity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "room")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Room extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, nullable = false, unique = true)
    private String name;
    @Column(length = 100, nullable = false)
    private String location;
    @Column(nullable = false)
    private Integer capacity;

    @ManyToOne
    @JoinColumn(name = "manager_group_id", nullable = false)
    private ManagerGroup managerGroup;

    @ManyToOne
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomType roomType;

    @ManyToOne
    @JoinColumn(name = "house_id", nullable = false)
    private House house;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<RoomImage> images;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "room")
    @JsonIgnore
    private List<Booking> bookings;
}
