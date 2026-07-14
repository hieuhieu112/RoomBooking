package com.app.backend.entity.baseEntity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "created_by", nullable = false)
    private Integer createdBy;
    @Column(name = "modify_at", nullable = false)
    private LocalDateTime modifyAt = LocalDateTime.now();
    @Column(name = "modify_by", nullable = false)
    private Integer modifyBy;
    @Version
    @Column(nullable = false)
    private Integer version = 1;
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}
