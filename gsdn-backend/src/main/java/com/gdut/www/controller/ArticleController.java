package com.gdut.www.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.gdut.www.domain.dto.ArticleForm;
import com.gdut.www.domain.entity.Article;
import com.gdut.www.domain.model.Response;
import com.gdut.www.service.ArticleService;
import com.gdut.www.utils.PageUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * @author chocoh
 */
@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping("/post")
    public Response post(@RequestBody @Validated ArticleForm articleForm) {
        Article article = Article.builder()
                .userId(StpUtil.getLoginIdAsLong())
                .images(String.join(",", articleForm.getImg()))
                .build();
        BeanUtils.copyProperties(articleForm, article);
        articleService.save(article);
        return Response.success();
    }

    @PostMapping("/update")
    public Response update(@RequestBody @Validated ArticleForm articleForm) {
        Article article = articleService.getById(articleForm.getId());
        if (article.getUserId().equals(StpUtil.getLoginIdAsLong())) {
            BeanUtils.copyProperties(articleForm, article);
            article.setImages(String.join(",", articleForm.getImg()));
            article.setUpdateTime(LocalDateTime.now());
            articleService.updateById(article);
            return Response.success();
        } else {
            return Response.error();
        }
    }

    @PostMapping("/delete")
    public Response delete(@RequestParam("id") Long id) {
        Article article = articleService.getById(id);
        if (article.getUserId().equals(StpUtil.getLoginIdAsLong())) {
            articleService.removeById(id);
            return Response.success();
        } else {
            return Response.error();
        }
    }

    @GetMapping("/detail")
    public Response detail(@RequestParam("id") Long id) {
        return Response.success(articleService.detail(articleService.getById(id)));
    }


    @PostMapping("/collect")
    public Response collect(@RequestParam("articleId") Long articleId) {
        articleService.collect(articleId);
        return Response.success();
    }

    @PostMapping("/like")
    public Response like(@RequestParam("articleId") Long articleId) {
        articleService.like(articleId);
        return Response.success();
    }

    @GetMapping("/idCollected")
    public Response isCollected(@RequestParam("articleId") Long articleId) {
        return Response.success(articleService.isCollected(articleId));
    }

    @GetMapping("/isLiked")
    public Response isLiked(@RequestParam("articleId") Long articleId) {
        return Response.success(articleService.isLiked(articleId));
    }

    @GetMapping("/getTypes")
    public Response getTypes() {
        return Response.success(articleService.getTypes());
    }

    @GetMapping("/related")
    public Response related(@RequestParam("type") String type) {
        return Response.success(articleService.related(type));
    }

    @GetMapping("/search")
    public Response search(@RequestParam("page") Integer page,
                           @RequestParam("pageSize") Integer pageSize,
                           @RequestParam("key") String key) {
        return Response.success(PageUtil.getPageBean(articleService.search(key), page, pageSize));
    }

    @GetMapping("/all")
    public Response all(@RequestParam("page") Integer page,
                        @RequestParam("pageSize") Integer pageSize) {
        return Response.success(PageUtil.getPageBean(articleService.all(), page, pageSize));
    }

    @GetMapping("/me")
    public Response me(@RequestParam("page") Integer page,
                       @RequestParam("pageSize") Integer pageSize) {
        return Response.success(PageUtil.getPageBean(articleService.me(), page, pageSize));
    }

    @GetMapping("/user")
    public Response user(@RequestParam("page") Integer page,
                         @RequestParam("pageSize") Integer pageSize,
                         @RequestParam("userId") Long userId) {
        return Response.success(PageUtil.getPageBean(articleService.user(userId), page, pageSize));
    }

    @GetMapping("/type")
    public Response type(@RequestParam("pageSize") Integer pageSize,
                         @RequestParam("page") Integer page,
                         @RequestParam("type") String type) {
        return Response.success(PageUtil.getPageBean(articleService.type(type), page, pageSize));
    }
}
