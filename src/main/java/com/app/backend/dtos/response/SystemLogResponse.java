package com.app.backend.dtos.response;

import java.time.LocalDateTime;

import com.app.backend.entity.enumm.LogAction;
import com.app.backend.entity.enumm.TargetType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SystemLogResponse {
    private Integer id;
    private Integer userId;
    private String action;
    private String targetType;
    private LocalDateTime timestamp;
    private Boolean isDeleted;
    private String description;
}
