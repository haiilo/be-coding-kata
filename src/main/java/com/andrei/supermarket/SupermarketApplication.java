package com.andrei.supermarket;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Supermarket API", version = "1.0", description = "API for managing supermarket cart and products"))
public class SupermarketApplication {
    public static void main(String[] args) {
        SpringApplication.run(SupermarketApplication.class, args);
    }
}
