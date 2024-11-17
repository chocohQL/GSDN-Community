package com.gdut.www.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.gdut.www.domain.dto.AiChatConversationReq;
import com.gdut.www.domain.entity.AiChatConversation;
import com.gdut.www.mapper.AiChatConversationMapper;
import com.gdut.www.service.AiChatConversationService;
import com.gdut.www.service.AiChatMessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AiChatConversationServiceImpl implements AiChatConversationService {
    @Resource
    private AiChatConversationMapper aiChatConversationMapper;
    @Resource
    private AiChatMessageService aiChatMessageService;

    @Override
    public Long createConversation(AiChatConversationReq req) {
        AiChatConversation aiChatConversation = new AiChatConversation();
        aiChatConversation.setTitle(req.getTitle());
        aiChatConversation.setUserId(StpUtil.getLoginIdAsLong());
        aiChatConversationMapper.insert(aiChatConversation);
        return aiChatConversation.getId();
    }

    @Override
    public void updateConversation(AiChatConversationReq req) {
        AiChatConversation aiChatConversation = aiChatConversationMapper.selectById(req.getId());
        aiChatConversation.setTitle(req.getTitle());
        aiChatConversationMapper.updateById(aiChatConversation);
    }

    @Override
    public AiChatConversation getConversation(Long id) {
        return aiChatConversationMapper.selectById(id);
    }

    @Override
    public List<AiChatConversation> getConversationList() {
        return aiChatConversationMapper.selectListByUserId(StpUtil.getLoginIdAsLong());
    }

    @Override
    public void deleteConversation(Long id) {
        aiChatMessageService.deleteChatMessageByConversationId(id);
        aiChatConversationMapper.deleteById(id);
    }
}
