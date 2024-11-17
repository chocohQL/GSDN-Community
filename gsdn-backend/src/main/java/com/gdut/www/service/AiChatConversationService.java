package com.gdut.www.service;


import com.gdut.www.domain.dto.AiChatConversationReq;
import com.gdut.www.domain.entity.AiChatConversation;

import java.util.List;

public interface AiChatConversationService {
    AiChatConversation getConversation(Long id);

    List<AiChatConversation> getConversationList();

    Long createConversation(AiChatConversationReq req);

    void updateConversation(AiChatConversationReq req);

    void deleteConversation(Long id);
}
