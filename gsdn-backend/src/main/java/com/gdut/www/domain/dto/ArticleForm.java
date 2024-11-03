package com.gdut.www.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author chocoh
 */
@Data
public class ArticleForm {
    private Long id;
    private List<String> img;
    @NotNull
    private String title;
    @NotNull
    private String type;
    @NotNull
    private String content;
    @NotNull
    private String summary;
}
