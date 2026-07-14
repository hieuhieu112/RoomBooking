package com.app.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "manufacturerdevice")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ManufacturerDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, unique = true, nullable = true)
    private String name;

    @OneToMany(mappedBy = "manufacturerDevice",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<DeviceModel> deviceModels ;
}
