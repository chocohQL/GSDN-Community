package com.gdut.www.controller;

import com.gdut.www.domain.dto.AiChatConversationReq;
import com.gdut.www.domain.model.Response;
import com.gdut.www.service.AiChatConversationService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.gdut.www.domain.model.Response.success;

@RestController
@RequestMapping("/ai/chat/conversation")
public class AiChatConversationController {
    @Resource
    private AiChatConversationService chatConversationService;

    @GetMapping("/get")
    public Response getChatConversation(@RequestParam("id") Long id) {
        return success(chatConversationService.getConversation(id));
    }

    @GetMapping("/list")
    public Response getConversationList() {
        return success(chatConversationService.getConversationList());
    }

    @PostMapping("/create")
    public Response createConversation(AiChatConversationReq req) {
        return success(chatConversationService.createConversation(req));
    }

    @PostMapping("/update")
    public Response updateConversation(AiChatConversationReq req) {
        chatConversationService.updateConversation(req);
        return success();
    }

    @PostMapping("/delete")
    public Response deleteChatConversationMy(@RequestParam("id") Long id) {
        chatConversationService.deleteConversation(id);
        return success(true);
    }
}
