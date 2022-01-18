package org.example.grfn;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Value("${spring.application.name}")
    private String appName;

    public String getAppName(){ return appName; }
}
