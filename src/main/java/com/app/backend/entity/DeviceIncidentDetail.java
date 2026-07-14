package com.app.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "deviceincidentdetail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceIncidentDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "device_incident",nullable = false)
    private DeviceIncident deviceIncident;

    @ManyToOne
    @JoinColumn(name = "device_id",nullable = false)
    private Device device;
}
