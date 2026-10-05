package co.tullave.rcg.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI tullaveRechargeOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("tuLlave Recharge API")
                        .version("v1")
                        .description("API REST para registrar y consultar recargas digitales tuLlave."));
    }
}
