package com.littlebank.finance.global.portone.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "portone")
public class PortoneProperties {
    private String apiKey;
    private String apiSecret;
}
