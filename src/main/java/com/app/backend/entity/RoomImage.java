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

    @Column(nullable = false,name = "stored_name", length = 100)
    private String storedName;

    @Column(length = 100, name = "original_name", nullable = false)
    private String originalName;

    @Column(nullable = false, name = "display_order")
    private Integer displayOrder;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    public RoomImage(String url, Room room) {
        this.url = url;
        this.room = room;
    }
}
