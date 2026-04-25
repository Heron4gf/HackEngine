package it.unicam.ids2026.api;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configurazione OpenAPI per la documentazione interattiva dell'API.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI hackEngineOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("HackEngine API")
                        .description("API per la gestione degli hackathon")
                        .version("1.0"));
    }
}
