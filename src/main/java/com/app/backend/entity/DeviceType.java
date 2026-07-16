package com.app.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "device_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "deviceType", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<DeviceModel> deviceModels;
}
