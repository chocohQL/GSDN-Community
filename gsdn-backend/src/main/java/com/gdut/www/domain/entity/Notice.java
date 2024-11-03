package com.gdut.www.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author chocoh
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notice {
    private Long id;
    private Long userId;
    private String content;
    private Integer type;
    private Integer status;
    private Boolean read;
    private LocalDateTime createTime;
}
