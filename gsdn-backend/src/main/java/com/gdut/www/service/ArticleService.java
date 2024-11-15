package com.gdut.www.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.gdut.www.domain.entity.Article;
import com.gdut.www.domain.entity.ArticleType;
import com.gdut.www.domain.vo.ArticleDetail;

import java.util.List;

/**
 * @author chocoh
 */
public interface ArticleService extends IService<Article> {
    ArticleDetail detail(Article article);

    void collect(Long articleId);

    void like(Long articleId);

    boolean isCollected(Long articleId);

    boolean isLiked(Long articleId);

    List<ArticleType> getTypes();

    List<ArticleDetail> related(String type);

    List<ArticleDetail> search(String key);

    List<ArticleDetail> all();

    List<ArticleDetail> me();

    List<ArticleDetail> user(Long userId);

    List<ArticleDetail> type(String type);
}
