package com.gdut.www.service;

import com.gdut.www.domain.entity.AiChatMessage;
import com.gdut.www.domain.model.Response;
import com.gdut.www.domain.dto.AiChatMessageReq;
import reactor.core.publisher.Flux;

import java.util.List;

public interface AiChatMessageService {
    Flux<Response> sendChatMessageStream(AiChatMessageReq aiChatMessageReq);

    List<AiChatMessage> getChatMessageListByConversationId(Long conversationId);

    void deleteChatMessage(Long id);

    void deleteChatMessageByConversationId(Long conversationId);
}
