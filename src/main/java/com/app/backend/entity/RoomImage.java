package com.app.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roomimage")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, nullable = false)
    private String url;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    public RoomImage(String url, Room room) {
        this.url = url;
        this.room = room;
    }
}
