package com.gdut.www.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(TongYiConnectionProperties.CONFIG_PREFIX)
public class TongYiConnectionProperties {

	public static final String CONFIG_PREFIX = "ai.tongyi";

	private String apiKey;

	private String systemValue;

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

}
