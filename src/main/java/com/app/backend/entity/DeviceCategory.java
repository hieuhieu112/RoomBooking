package com.app.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "device_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, nullable = false, unique = true)
    private String name;


    @OneToMany(mappedBy = "deviceCategory", cascade = CascadeType.ALL)
    private List<Device> device;
}
