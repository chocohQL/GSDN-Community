package com.gdut.www.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author chocoh
 */
@Data
public class AiChatMessageReq {
    @NotNull
    private Long conversationId;
    @NotNull
    private String content;
}
