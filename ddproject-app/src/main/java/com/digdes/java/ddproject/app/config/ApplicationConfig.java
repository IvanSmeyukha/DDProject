package com.digdes.java.ddproject.app.config;

import com.digdes.java.ddproject.mapping.config.MappingConfig;
import com.digdes.java.ddproject.repositories.config.RepositoryConfig;
import com.digdes.java.ddproject.services.config.ServiceConfig;
import com.digdes.java.ddproject.web.config.ControllerConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        RepositoryConfig.class,
        MappingConfig.class,
        ServiceConfig.class,
        ControllerConfig.class
})
public class ApplicationConfig {
}
