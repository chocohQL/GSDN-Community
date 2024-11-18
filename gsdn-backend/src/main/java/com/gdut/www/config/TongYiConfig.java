package com.gdut.www.config;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.common.MessageManager;
import com.gdut.www.client.TongYiChatModel;
import com.gdut.www.config.properties.TongYiChatOptions;
import org.springframework.ai.chat.model.StreamingChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chocoh
 */
@Configuration
public class TongYiConfig {
    @Bean
    public StreamingChatModel chatModel() {
        return new TongYiChatModel(new Generation(), new TongYiChatOptions());
    }

    @Bean
    public MessageManager messageManager() {
        return new MessageManager();
    }
}
