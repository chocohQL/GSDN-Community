package com.gdut.www.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gdut.www.domain.dto.CommentForm;
import com.gdut.www.domain.entity.Comment;
import com.gdut.www.domain.model.Response;
import com.gdut.www.service.CommentService;
import com.gdut.www.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chocoh
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @RequestMapping("/post")
    public Response post(@RequestBody @Validated CommentForm commentForm) {
        commentService.post(commentForm);
        return Response.success();
    }

    @RequestMapping("/delete")
    public Response delete(@RequestParam("commentId") Long commentId) {
        Comment comment = commentService.getById(commentId);
        if (comment.getUserId().equals(StpUtil.getLoginIdAsLong())) {
            commentService.removeById(commentId);
            commentService.remove(new LambdaQueryWrapper<Comment>().eq(Comment::getParentId, commentId));
            return Response.success();
        } else {
            return Response.error();
        }
    }

    @RequestMapping("/first")
    public Response first(@RequestParam("page") Integer page,
                          @RequestParam("pageSize") Integer pageSize,
                          @RequestParam("articleId") Long articleId) {
        return Response.success(PageUtil.getPageBean(commentService.first(articleId), page, pageSize));
    }

    @RequestMapping("/multi")
    public Response multi(@RequestParam("commonId") Long commonId) {
        return Response.success(commentService.multi(commonId));
    }
}
