package com.devshowcase.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI devShowcaseOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("DevShowcase API")
                        .description(
                            "API REST para gerenciamento de portfólios de desenvolvedores. " +
                            "Permite cadastrar perfis, projetos e tecnologias, além de registrar feedbacks com notas e dar upvotes em projetos.\n\n" +
                            "## Fluxo de uso recomendado\n" +
                            "1. Cadastre as **Tecnologias** disponíveis (ex: Java, React)\n" +
                            "2. Crie um **Perfil** de desenvolvedor\n" +
                            "3. Crie **Projetos** associando-os ao perfil e às tecnologias\n" +
                            "4. Interaja com os projetos via **upvotes** e **feedbacks**"
                        )
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("DevShowcase")
                                .email("contato@devshowcase.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server().url("/").description("Servidor atual")
                ));
    }
}
