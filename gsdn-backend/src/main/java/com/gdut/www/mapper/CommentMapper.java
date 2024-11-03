package com.gdut.www.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gdut.www.domain.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author chocoh
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
