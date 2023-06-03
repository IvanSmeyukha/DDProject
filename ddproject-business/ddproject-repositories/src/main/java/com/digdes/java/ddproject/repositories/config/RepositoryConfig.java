package com.digdes.java.ddproject.repositories.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"com.digdes.java.ddproject.repositories"})
public class RepositoryConfig {
}
