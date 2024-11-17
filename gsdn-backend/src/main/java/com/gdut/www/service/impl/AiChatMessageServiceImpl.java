package com.gdut.www.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.gdut.www.config.properties.TongYiChatOptions;
import com.gdut.www.domain.entity.AiChatConversation;
import com.gdut.www.domain.entity.AiChatMessage;
import com.gdut.www.domain.model.Response;
import com.gdut.www.domain.dto.AiChatMessageReq;
import com.gdut.www.mapper.AiChatMessageMapper;
import com.gdut.www.service.AiChatConversationService;
import com.gdut.www.service.AiChatMessageService;
import com.gdut.www.utils.AiUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.StreamingChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.gdut.www.common.Constants.*;
import static com.gdut.www.common.ResponseMsg.AI_CONVERSATION_NOT_EXISTS;
import static com.gdut.www.common.ResponseMsg.AI_GENERATE_ERROR;

@Service
@Slf4j
public class AiChatMessageServiceImpl implements AiChatMessageService {
    @Resource
    private AiChatMessageMapper aiChatMessageMapper;
    @Resource
    private AiChatConversationService aiChatConversationService;
    @Resource
    private StreamingChatModel chatModel;

    @Override
    public Flux<Response> sendChatMessageStream(AiChatMessageReq aiChatMessageReq) {
        AiChatConversation conversation = aiChatConversationService.getConversation(aiChatMessageReq.getConversationId());
        if (conversation == null) {
            throw new RuntimeException(AI_CONVERSATION_NOT_EXISTS);
        }
        List<AiChatMessage> historyMessages = aiChatMessageMapper.selectListByConversationId(conversation.getId());

        Long userId = StpUtil.getLoginIdAsLong();
        AiChatMessage userMessage = createChatMessage(conversation.getId(), userId, null, aiChatMessageReq.getContent(), MessageType.USER);
        AiChatMessage assistantMessage = createChatMessage(conversation.getId(), userId, userMessage.getId(), "", MessageType.ASSISTANT);

        Prompt prompt = buildPrompt(historyMessages, aiChatMessageReq);

        Flux<ChatResponse> streamResponse = chatModel.stream(prompt);
        StringBuffer contentBuffer = new StringBuffer();
        return streamResponse.map(chunk -> {
                    String newContent = chunk.getResult() != null ? chunk.getResult().getOutput().getContent() : null;
                    contentBuffer.append(StrUtil.nullToDefault(newContent, ""));
                    assistantMessage.setContent(newContent);
                    return Response.success(assistantMessage);
                })
                .doOnComplete(() -> {
                    assistantMessage.setContent(contentBuffer.toString());
                    aiChatMessageMapper.updateById(assistantMessage);
                })
                .doOnError(throwable -> {
                    assistantMessage.setContent(throwable.getMessage());
                    aiChatMessageMapper.updateById(assistantMessage);
                })
                .onErrorResume(error -> Flux.just(Response.error(AI_GENERATE_ERROR)));
    }

    private Prompt buildPrompt(List<AiChatMessage> messages, AiChatMessageReq sendReqVO) {
        List<Message> chatMessages = new ArrayList<>();
        chatMessages.add(new SystemMessage(AI_SYSTEM_VALUE));
        filterContextMessages(messages).forEach(message -> chatMessages.add(AiUtil.buildMessage(message.getType(), message.getContent())));
        chatMessages.add(new UserMessage(sendReqVO.getContent()));
        return new Prompt(chatMessages, new TongYiChatOptions());
    }

    private List<AiChatMessage> filterContextMessages(List<AiChatMessage> messages) {
        List<AiChatMessage> contextMessages = new ArrayList<>();
        for (int i = messages.size() - 1; i >= 0; i--) {
            AiChatMessage assistantMessage = CollUtil.get(messages, i);
            if (assistantMessage == null || assistantMessage.getReplyId() == null) {
                continue;
            }
            AiChatMessage userMessage = CollUtil.get(messages, i - 1);
            if (userMessage == null || ObjUtil.notEqual(assistantMessage.getReplyId(), userMessage.getId())
                    || StrUtil.isEmpty(assistantMessage.getContent())) {
                continue;
            }
            contextMessages.add(assistantMessage);
            contextMessages.add(userMessage);
            if (contextMessages.size() >= AI_MAX_CONVERSATION_MESSAGE) {
                break;
            }
        }
        Collections.reverse(contextMessages);
        return contextMessages;
    }

    private AiChatMessage createChatMessage(Long conversationId, Long userId, Long replyId, String content, MessageType type) {
        AiChatMessage aiChatMessage = AiChatMessage.builder()
                .conversationId(conversationId)
                .userId(userId)
                .replyId(replyId)
                .content(content)
                .type(type.getValue())
                .build();
        aiChatMessageMapper.insert(aiChatMessage);
        return aiChatMessage;
    }

    @Override
    public List<AiChatMessage> getChatMessageListByConversationId(Long conversationId) {
        return aiChatMessageMapper.selectListByConversationId(conversationId);
    }

    @Override
    public void deleteChatMessage(Long id) {
        aiChatMessageMapper.deleteById(id);
    }

    @Override
    public void deleteChatMessageByConversationId(Long conversationId) {
        aiChatMessageMapper.deleteByConversationId(conversationId);
    }
}
