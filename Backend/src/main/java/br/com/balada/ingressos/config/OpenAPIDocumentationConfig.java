package br.com.balada.ingressos.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIDocumentationConfig {
    @Bean
    public OpenAPI customOpenAPI() {
/*
        //COM adição de Segurança - JWT
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI().addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components().addSecuritySchemes(securitySchemeName,
                        new SecurityScheme().name(securitySchemeName).type(SecurityScheme.Type.HTTP).scheme("bearer")
                                .bearerFormat("JWT")))
                .info(new Info().title("API - Vendas de Ingressos On-line")
                        .contact(new Contact().name("Equipe Matador").email("matador@balada.com.br").url("balada.com.br/ingressos"))
                        .description("Sistema de Vendas de Ingressos On-line")
                        .version("v0.0.1"))
                .externalDocs(new ExternalDocumentation().description("Documentação")
                        .url("https://www.balada.edu.br/docs/open-api"));
    }

 */
        //SEM adição de Segurança - JWT
        return new OpenAPI()
                .info(new Info()
                        .title("API - Vendas de Ingressos On-line")
                        .contact(new Contact()
                                .name("Equipe Matador")
                                .email("matador@balada.com.br")
                                .url("balada.com.br/ingressos"))
                        .description("Sistema de Vendas de Ingressos On-line")
                        .version("v0.0.1"))
                .externalDocs(new ExternalDocumentation()
                        .description("Documentação")
                        .url("https://www.balada.edu.br/docs/open-api"));
    }
}