package com.ensa.notes.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Note Management API REST")
                        .version("1.0.0")
                        .description("API REST (Notes)")
                        .contact(new Contact()
                                .name("Support Technique")
                                .email("ettarak.zouhair@gmail.com")));
    }
}
