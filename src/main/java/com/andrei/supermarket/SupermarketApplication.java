package com.andrei.supermarket;

import com.andrei.supermarket.domain.Cart;
import com.andrei.supermarket.model.ProductModel;
import com.andrei.supermarket.repository.ProductRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.IOException;
import java.util.List;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Supermarket API", version = "1.0", description = "API for managing supermarket cart and products"))
public class SupermarketApplication {
    public static void main(String[] args) {
        SpringApplication.run(SupermarketApplication.class, args);
    }
    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository) {
        return args -> {
            if (productRepository.count() == 0) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    List<ProductModel> products = objectMapper.readValue(
                            new File("src/main/resources/products.json"),
                            new TypeReference<>() {}
                    );
                    productRepository.saveAll(products);
                } catch (IOException e) {
                    System.err.println("Failed to load initial products: " + e.getMessage());
                }
            }
        };
    }
    // assuming we are working with a single cart,
    // API for demonstration purposes only
    @Bean
    Cart createCart() {
        return new Cart();
    }
}
