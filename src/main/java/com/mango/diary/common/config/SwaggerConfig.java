package com.mango.diary.common.config;

import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Server devServer = new Server()
                .url("http://localhost:8080")
                .description("Development Server");

        Server prodServer = new Server()
                .url("https://mangolion-server.site")
                .description("Production Server");

        return new OpenAPI()
                .info(new Info().title("아프지망고 API").version("1.0").description("아프지망고 API 자동 배포 테스트2"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth", new io.swagger.v3.oas.models.security.SecurityScheme()
                                .name("bearerAuth")
                                .type(Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .servers(Arrays.asList(prodServer, devServer));
    }

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("mango")
                .pathsToMatch("/**")
                .addOpenApiCustomizer(openApiCustomizer())
                .build();
    }

    private OpenApiCustomizer openApiCustomizer() {
        return openApi -> {
            openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
                operation.addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
            }));
        };
    }
}
