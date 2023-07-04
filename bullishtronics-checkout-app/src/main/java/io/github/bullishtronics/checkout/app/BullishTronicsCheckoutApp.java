package io.github.bullishtronics.checkout.app;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Bullishtronics Ltd", version = "1.0", description = "Bullishtronics Swagger"))
@SecurityScheme(name = "basicAuth", scheme = "basic", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class BullishTronicsCheckoutApp {

    public static void main(String[] args) {
        SpringApplication.run(BullishTronicsCheckoutApp.class, args);
    }
}
