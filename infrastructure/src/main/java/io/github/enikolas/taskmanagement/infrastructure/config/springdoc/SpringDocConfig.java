package io.github.enikolas.taskmanagement.infrastructure.config.springdoc;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {
    @Bean
    public OpenAPI taskManagementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Task Management API")
                        .description("Task Management API is a RESTful service inspired by Trello, designed to " +
                                "manage boards, lists, and cards.")
                        .license(new License()
                                .name("MIT")
                                .url("https://github.com/enikolas/task-management-api/blob/main/LICENSE")));
    }
}
