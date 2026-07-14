package com.app.backend.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.app.backend.entity.baseEntity.BaseEntity;
import com.app.backend.entity.enumm.DeviceIndecentStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "deviceincident")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceIncident extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, name = "time_incident")
    private LocalDateTime timeIncident;

    @Column(nullable = false)
    private DeviceIndecentStatus status = DeviceIndecentStatus.INPROCESS;


    @ManyToOne
    @JoinColumn(name = "incident_by", nullable = false)
    private User incidentBy;

    @OneToMany(mappedBy = "deviceIncident", cascade = CascadeType.ALL)
    private List<DeviceIncidentDetail> incidentList;
}
