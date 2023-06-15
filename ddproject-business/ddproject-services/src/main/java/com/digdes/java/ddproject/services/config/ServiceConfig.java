package com.digdes.java.ddproject.services.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.digdes.java.ddproject.services")
@EnableConfigurationProperties(RabbitMQConfig.class)
public class ServiceConfig {
}
