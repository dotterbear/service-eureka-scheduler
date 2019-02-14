package com.dotterbear.service.eureka.scheduler.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SchedulerPropertiesConfig.class)
public class PropertiesConfig {}
