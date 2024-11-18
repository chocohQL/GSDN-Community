package com.gdut.www.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(TongYiProperties.CONFIG_PREFIX)
public class TongYiProperties {

    public static final String CONFIG_PREFIX = "ai.tongyi";

    private String apiKey;
}
