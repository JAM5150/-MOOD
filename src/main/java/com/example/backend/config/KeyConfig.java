package com.example.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class KeyConfig {

    private final Environment environment;

    public String getGCPKey(){
        return environment.getProperty("GCP-API-KEY");
    }
}
