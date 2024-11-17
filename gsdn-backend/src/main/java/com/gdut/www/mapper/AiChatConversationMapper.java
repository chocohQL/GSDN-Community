package com.gdut.www.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gdut.www.domain.entity.AiChatConversation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AiChatConversationMapper extends BaseMapper<AiChatConversation> {
    default List<AiChatConversation> selectListByUserId(Long userId) {
        return selectList(new LambdaQueryWrapper<AiChatConversation>()
                .eq(AiChatConversation::getUserId, userId)
        );
    }
}
