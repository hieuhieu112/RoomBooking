package com.app.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "devicemodel")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 100, unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String specifications;

    @ManyToOne
    @JoinColumn(name = "manufacturer_device_id", nullable = false)
    private ManufacturerDevice manufacturerDevice;

    @ManyToOne
    @JoinColumn(name = "device_type_id", nullable = false)
    private DeviceType deviceType;

    @OneToMany(mappedBy = "deviceModel", cascade = CascadeType.ALL)
    private List<Device> device;
}
