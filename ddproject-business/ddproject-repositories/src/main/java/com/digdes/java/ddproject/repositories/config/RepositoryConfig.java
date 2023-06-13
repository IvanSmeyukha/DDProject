package com.digdes.java.ddproject.repositories.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = {"com.digdes.java.ddproject.model"})
@EnableJpaRepositories(basePackages = {"com.digdes.java.ddproject.repositories"})
public class RepositoryConfig {
}
