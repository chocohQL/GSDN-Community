package com.gdut.www.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gdut.www.domain.dto.CommentReq;
import com.gdut.www.domain.entity.Comment;

import java.util.List;

/**
 * @author chocoh
 */
public interface CommentService extends IService<Comment> {
    void post(CommentReq commentReq);

    List<Comment> first(Long articleId);

    List<Comment> multi(Long commentId);
}
