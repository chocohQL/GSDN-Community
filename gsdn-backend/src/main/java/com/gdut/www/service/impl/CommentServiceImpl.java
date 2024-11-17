package com.gdut.www.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdut.www.domain.dto.CommentReq;
import com.gdut.www.domain.entity.Comment;
import com.gdut.www.mapper.CommentMapper;
import com.gdut.www.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author chocoh
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Override
    public void post(CommentReq commentReq) {
        save(Comment.builder()
                .articleId(commentReq.getArticleId())
                .content(commentReq.getContent())
                .userId(StpUtil.getLoginIdAsLong())
                .parentId(commentReq.getParentId() == null || commentReq.getParentId() <= 0 ?
                        0 : commentReq.getParentId())
                .build());
    }

    @Override
    public List<Comment> first(Long articleId) {
        return list(new LambdaQueryWrapper<Comment>().eq(Comment::getArticleId, articleId).eq(Comment::getParentId, 0));
    }

    @Override
    public List<Comment> multi(Long commentId) {
        return list(new LambdaQueryWrapper<Comment>().eq(Comment::getParentId, commentId));
    }
}
