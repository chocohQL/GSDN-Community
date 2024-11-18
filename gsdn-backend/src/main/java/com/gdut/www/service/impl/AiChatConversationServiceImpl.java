package com.gdut.www.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
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
        aiChatConversation.setTitle(StrUtil.nullToDefault(req.getTitle(), AiChatConversation.TITLE_DEFAULT));
        aiChatConversation.setUserId(StpUtil.getLoginIdAsLong());
        aiChatConversationMapper.insert(aiChatConversation);
        return aiChatConversation.getId();
    }

    @Override
    public void updateConversation(AiChatConversationReq req) {
        if (req.getId() == null) {
            return;
        }
        AiChatConversation aiChatConversation = aiChatConversationMapper.selectById(req.getId());
        if (aiChatConversation == null || aiChatConversation.getUserId() != StpUtil.getLoginIdAsLong()) {
            return;
        }
        aiChatConversation.setTitle(req.getTitle() == null ? aiChatConversation.getTitle() : req.getTitle());
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
        AiChatConversation aiChatConversation = aiChatConversationMapper.selectById(id);
        if (aiChatConversation == null || aiChatConversation.getUserId() != StpUtil.getLoginIdAsLong()) {
            return;
        }
        aiChatMessageService.deleteChatMessageByConversationId(id);
        aiChatConversationMapper.deleteById(id);
    }
}
