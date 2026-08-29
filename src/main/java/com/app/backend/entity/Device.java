package com.app.backend.entity;

import com.app.backend.entity.baseEntity.BaseEntity;
import com.app.backend.entity.enumm.DeviceStatus;
import com.app.backend.entity.enumm.DeviceTrackingType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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

    @Column(length = 100, nullable = false, unique = true)
    private String serial;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeviceTrackingType trackingType;

    private Integer quantity;


    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeviceStatus status = DeviceStatus.ACTIVE;


    @ManyToOne
    @JoinColumn(name = "device_category_id", nullable = true)
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

    public Boolean isValidStatus(){
        return status.equals(DeviceStatus.ACTIVE);
    }

    public void generateSerial(){
        this.serial = UUID.randomUUID().toString();
    }
}
