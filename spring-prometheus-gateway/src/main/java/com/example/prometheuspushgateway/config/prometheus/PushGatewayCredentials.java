package com.example.prometheuspushgateway.config.prometheus;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "push-gateway.credentials")
public class PushGatewayCredentials {

    private String username;
    private String password;
}
