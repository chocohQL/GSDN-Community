package com.gdut.www.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.gdut.www.domain.entity.Article;
import com.gdut.www.domain.entity.ArticleType;
import com.gdut.www.domain.dto.ArticleResp;

import java.util.List;

/**
 * @author chocoh
 */
public interface ArticleService extends IService<Article> {
    ArticleResp detail(Article article);

    void collect(Long articleId);

    void like(Long articleId);

    boolean isCollected(Long articleId);

    boolean isLiked(Long articleId);

    List<ArticleType> getTypes();

    List<ArticleResp> related(String type);

    List<ArticleResp> search(String key);

    List<ArticleResp> all();

    List<ArticleResp> me();

    List<ArticleResp> user(Long userId);

    List<ArticleResp> type(String type);
}
