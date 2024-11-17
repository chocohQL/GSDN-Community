package com.gdut.www.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdut.www.domain.entity.Article;
import com.gdut.www.domain.entity.ArticleType;
import com.gdut.www.domain.entity.Collected;
import com.gdut.www.domain.entity.Liked;
import com.gdut.www.domain.dto.ArticleResp;
import com.gdut.www.mapper.ArticleMapper;
import com.gdut.www.mapper.ArticleTypeMapper;
import com.gdut.www.mapper.CollectedMapper;
import com.gdut.www.mapper.LikedMapper;
import com.gdut.www.service.ArticleService;
import com.gdut.www.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chocoh
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    private UserService userService;
    @Autowired
    private CollectedMapper collectedMapper;
    @Autowired
    private LikedMapper likedMapper;
    @Autowired
    private ArticleTypeMapper articleTypeMapper;
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public ArticleResp detail(Article article) {
        ArticleResp articleResp = ArticleResp.builder()
                .userResp(userService.getUserInfo(userService.getById(article.getUserId())))
                .img(StringUtils.isNotBlank(article.getImages()) ?
                        Arrays.asList(article.getImages().split(",")) : new ArrayList<>())
                .collected(collectedMapper.selectCount(new LambdaQueryWrapper<Collected>()
                        .eq(Collected::getArticleId, article.getId())))
                .liked(likedMapper.selectCount(new LambdaQueryWrapper<Liked>()
                        .eq(Liked::getArticleId, article.getId())))
                .build();
        BeanUtils.copyProperties(article, articleResp);
        return articleResp;
    }

    @Override
    public void collect(Long articleId) {
        Collected collected = getCollected(articleId);
        if (collected == null) {
            collectedMapper.insert(Collected.builder()
                    .userId(StpUtil.getLoginIdAsLong())
                    .articleId(articleId)
                    .build());
        } else {
            collectedMapper.deleteById(collected);
        }
    }

    @Override
    public void like(Long articleId) {
        Liked liked = getLiked(articleId);
        if (liked == null) {
            likedMapper.insert(Liked.builder()
                    .userId(StpUtil.getLoginIdAsLong())
                    .articleId(articleId)
                    .build());
        } else {
            likedMapper.deleteById(liked);
        }
    }

    @Override
    public List<ArticleType> getTypes() {
        return articleTypeMapper.selectList(null);
    }

    @Override
    public List<ArticleResp> related(String type) {
        long id = StpUtil.getLoginIdAsLong();
        List<Long> ids;
        if ("like".equals(type)) {
            ids = likedMapper.selectList(new LambdaQueryWrapper<Liked>().eq(Liked::getUserId, id)).stream()
                    .map(Liked::getArticleId)
                    .collect(Collectors.toList());
        } else if ("collect".equals(type)) {
            ids = collectedMapper.selectList(new LambdaQueryWrapper<Collected>().eq(Collected::getUserId, id)).stream()
                    .map(Collected::getArticleId)
                    .collect(Collectors.toList());
        } else {
            return null;
        }
        List<ArticleResp> articleRespList = new ArrayList<>();
        ids.forEach(i -> articleRespList.add(detail(getById(i))));
        return articleRespList;
    }

    @Override
    public List<ArticleResp> search(String key) {
        List<ArticleResp> articleRespList = new ArrayList<>();
        articleMapper.selectList(new LambdaQueryWrapper<Article>()
                        .like(Article::getTitle, key)
                        .or()
                        .like(Article::getContent, key)
                        .or()
                        .like(Article::getType, key)
                        .or()
                        .like(Article::getSummary, key))
                .forEach(article -> articleRespList.add(detail(article)));
        return articleRespList;
    }

    @Override
    public List<ArticleResp> all() {
        List<Article> articles = list();
        List<ArticleResp> articleRespList = new ArrayList<>();
        articles.forEach(article -> articleRespList.add(detail(article)));
        return articleRespList;
    }

    @Override
    public List<ArticleResp> me() {
        return all().stream().filter(i -> i.getUserId().equals(StpUtil.getLoginIdAsLong())).collect(Collectors.toList());
    }

    @Override
    public List<ArticleResp> user(Long userId) {
        return all().stream().filter(i -> i.getUserId().equals(userId)).collect(Collectors.toList());
    }

    @Override
    public List<ArticleResp> type(String type) {
        return all().stream().filter(i -> type.equals(i.getType())).collect(Collectors.toList());
    }

    @Override
    public boolean isCollected(Long articleId) {
        return getCollected(articleId) != null;
    }

    @Override
    public boolean isLiked(Long articleId) {
        return getLiked(articleId) != null;
    }

    private Collected getCollected(Long articleId) {
        return collectedMapper.selectOne(new LambdaQueryWrapper<Collected>()
                .eq(Collected::getUserId, StpUtil.getLoginIdAsLong())
                .eq(Collected::getArticleId, articleId));
    }

    private Liked getLiked(Long articleId) {
        return likedMapper.selectOne(new LambdaQueryWrapper<Liked>()
                .eq(Liked::getUserId, StpUtil.getLoginIdAsLong())
                .eq(Liked::getArticleId, articleId));
    }
}
