package com.gdut.www.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author chocoh
 */
@Data
public class CommentForm {
    @NotNull
    private Long articleId;
    @NotNull
    private Long parentId;
    @NotNull
    private String content;
}
