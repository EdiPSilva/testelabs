package br.com.TestLabs.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        final Server devServer = new Server();
        devServer.setUrl("http://localhost:8080/");
        devServer.setDescription("Url do ambiente local");

        final Contact contact = new Contact();
        contact.setEmail("edipaulinodasilva@gmail.com");
        contact.setName("Edi Silva");
        contact.setUrl("https://www.linkedin.com/in/edipsilva/");

        final Info info = new Info()
                .title("Teste Luiza Labs")
                .version("1.0")
                .contact(contact)
                .description("Esta API expões alguns endpoints para realizar o cadastramento de pedidos através de uma importação de arquivo.");

        return new OpenAPI().info(info).servers(List.of(devServer));
    }
}
