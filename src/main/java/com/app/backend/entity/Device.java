package com.app.backend.entity;

import com.app.backend.entity.baseEntity.BaseEntity;
import com.app.backend.entity.enumm.DeviceStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "device")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Device extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeviceStatus status = DeviceStatus.ACTIVE;


    @ManyToOne
    @JoinColumn(name = "device_category_id", nullable = false)
    private DeviceCategory deviceCategory;

    @ManyToOne
    @JoinColumn(name = "device_model_id", nullable = false)
    private DeviceModel deviceModel;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<DeviceBorrowDetail> deviceBorrowDetails;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<DeviceIncidentDetail>  deviceIncidentDetails;
}
