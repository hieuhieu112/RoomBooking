package com.app.backend.dtos.request;

import java.time.LocalDateTime;

import com.app.backend.entity.enumm.LogAction;
import com.app.backend.entity.enumm.TargetType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SystemLogRequest {
    private Integer userId;
    private LogAction action;
    private TargetType targetType;
    private LocalDateTime timestamp;
    private Boolean isDeleted;
    private String description;
}
