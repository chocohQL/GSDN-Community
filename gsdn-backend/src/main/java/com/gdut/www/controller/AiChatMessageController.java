package com.gdut.www.controller;

import com.gdut.www.domain.model.Response;
import com.gdut.www.domain.dto.AiChatMessageReq;
import com.gdut.www.service.AiChatConversationService;
import com.gdut.www.service.AiChatMessageService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;

import static com.gdut.www.domain.model.Response.success;

@RestController
@RequestMapping("/ai/chat/message")
public class AiChatMessageController {
    @Resource
    private AiChatMessageService chatMessageService;
    @Resource
    private AiChatConversationService chatConversationService;

    @PostMapping(value = "/send-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Response> sendChatMessageStream(@RequestBody AiChatMessageReq req) {
        return chatMessageService.sendChatMessageStream(req);
    }

    @GetMapping("/list-by-conversation-id")
    public Response getChatMessageListByConversationId(@RequestParam("conversationId") Long conversationId) {
        return success(chatConversationService.getConversation(conversationId) == null ? null : chatMessageService.getChatMessageListByConversationId(conversationId));
    }

    @DeleteMapping("/delete")
    public Response deleteChatMessage(@RequestParam("id") Long id) {
        chatMessageService.deleteChatMessage(id);
        return success();
    }

    @DeleteMapping("/delete-by-conversation-id")
    public Response deleteChatMessageByConversationId(@RequestParam("conversationId") Long conversationId) {
        chatMessageService.deleteChatMessageByConversationId(conversationId);
        return success();
    }
}
