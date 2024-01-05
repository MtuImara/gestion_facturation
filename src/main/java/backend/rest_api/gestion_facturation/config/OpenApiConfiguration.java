package backend.rest_api.gestion_facturation.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "API GESTION DE LA FACTURATION", version = "0.1", description = "MICROSERVICE, DESCRIPTION DES APIs UD BACKEND", contact = @Contact(name = "Tout-Autre", email = "hapanakujibu@gmail.com", url = "www.hapanakujibu.com")))
public class OpenApiConfiguration {

}
