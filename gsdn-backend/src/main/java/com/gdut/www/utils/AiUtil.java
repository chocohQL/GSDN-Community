package com.gdut.www.utils;

import cn.hutool.core.util.StrUtil;
import org.springframework.ai.chat.messages.*;

/**
 * @author chocoh
 */
public class AiUtil {
    public static Message buildMessage(String type, String content) {
        if (MessageType.USER.getValue().equals(type)) {
            return new UserMessage(content);
        }
        if (MessageType.ASSISTANT.getValue().equals(type)) {
            return new AssistantMessage(content);
        }
        if (MessageType.SYSTEM.getValue().equals(type)) {
            return new SystemMessage(content);
        }
        if (MessageType.FUNCTION.getValue().equals(type)) {
            return new FunctionMessage(content);
        }
        throw new IllegalArgumentException(StrUtil.format("未知消息类型({})", type));
    }
}
