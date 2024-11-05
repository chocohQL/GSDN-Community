package com.gdut.www.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author chocoh
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetails {
    private Long id;
    private Long userId;
    private LocalDate birthday;
    private String orgName;
    private Integer orgType;
    private String orgNumber;
    private Integer orgIdentityStatus;
    private String extraInfo;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
