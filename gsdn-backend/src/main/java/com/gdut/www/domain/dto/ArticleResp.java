package com.gdut.www.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author chocoh
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResp {
    private Long id;
    private Long userId;
    private String title;
    private String type;
    private List<String> img;
    private String content;
    private String summary;
    private Long collected;
    private Long liked;
    private Integer status;
    private UserResp userResp;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
