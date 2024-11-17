package com.gdut.www.domain.dto;

import lombok.Data;

/**
 * @author chocoh
 */
@Data
public class AiChatMessageReq {
    private Long conversationId;
    private String content;
}
