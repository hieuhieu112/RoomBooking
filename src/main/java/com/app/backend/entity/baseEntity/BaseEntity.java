package com.app.backend.entity.baseEntity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(name = "created_by", nullable = false)
    private Integer createdBy;

    @LastModifiedDate
    @Column(name = "modify_at", nullable = false)
    private LocalDateTime modifyAt;

    @LastModifiedBy
    @Column(name = "modify_by", nullable = false)
    private Integer modifyBy;

    @Version
    @Column(nullable = false)
    private Integer version;


    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

}
