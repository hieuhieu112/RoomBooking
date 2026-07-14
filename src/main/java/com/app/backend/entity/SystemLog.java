package com.app.backend.entity;

import java.beans.Transient;
import java.time.LocalDateTime;

import com.app.backend.entity.enumm.LogAction;
import com.app.backend.entity.enumm.TargetType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "systemlog")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SystemLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //user_ID, action, target_id, target_type

    @Column(nullable = false, name = "log_action")
    @Enumerated(EnumType.STRING)
    private LogAction logAction;

    @Column(nullable = false, name = "target_ype")
    @Enumerated(EnumType.STRING)
    private TargetType targetType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDelete = false;

    private LocalDateTime timestamp = LocalDateTime.now();
}
