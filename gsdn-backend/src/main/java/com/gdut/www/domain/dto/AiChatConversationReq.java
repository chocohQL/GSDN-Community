package com.gdut.www.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author chocoh
 */
@Data
public class AiChatConversationReq {
    private Long id;
    @NotNull
    private String title;
}
