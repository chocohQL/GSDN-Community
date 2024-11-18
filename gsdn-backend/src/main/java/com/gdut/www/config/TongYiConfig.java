package com.gdut.www.config;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.common.MessageManager;
import com.alibaba.dashscope.utils.Constants;
import com.gdut.www.client.TongYiChatModel;
import com.gdut.www.config.properties.TongYiChatOptions;
import com.gdut.www.config.properties.TongYiProperties;
import org.springframework.ai.chat.model.StreamingChatModel;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chocoh
 */
@Configuration
@SuppressWarnings("deprecation")
@EnableConfigurationProperties(TongYiProperties.class)
public class TongYiConfig {
    @Bean
    public StreamingChatModel chatModel(TongYiProperties tongYiProperties) {
        Constants.apiKey = tongYiProperties.getApiKey();
        return new TongYiChatModel(new Generation(), new TongYiChatOptions());
    }

    @Bean
    public MessageManager messageManager() {
        return new MessageManager();
    }
}
