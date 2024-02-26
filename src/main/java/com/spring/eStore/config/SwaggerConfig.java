package com.spring.eStore.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name="scheme1",
        type= SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@OpenAPIDefinition(
        info = @Info(
                title="Electronic Store",
                description = "Swagger UI for Electronic Store",
                contact = @Contact(
                        name="Suraj Jadli"
                )
        )
)
public class SwaggerConfig {

//    @Bean
//    public OpenAPI openAPI() {
//        return new OpenAPI()
//                .info(new Info().title("Electronic Store API")
//                        .description("This is Electronic store project")
//                        .version("1.0")
//                        .contact(new Contact().name("Suraj Jadli")));
//    }
}
