package org.ohrim.taskmanagementsystem.configuration;


import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Task Management System API")
                        .version("1.0")
                        .description("""
                            API documentation for Task Management System.
                            
                            **Contact Information:**
                            - **Developer Name:** Pavel Okhrimchuk
                            - **Email:** PavelOkhrimchuk@yandex.ru
                            - **Telegram:** [@ohrimmm](https://t.me/ohrimmm)
                            """))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"));
    }






}
