package com.andrei.supermarket.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProductTest {

    @Test
    void productConstructorThrowsExceptionForInvalidName() {
        assertThatThrownBy(() -> new Product(null, BigDecimal.valueOf(30), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Product name cannot be null or empty");

        assertThatThrownBy(() -> new Product("", BigDecimal.valueOf(30), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Product name cannot be null or empty");
    }

    @Test
    void productConstructorThrowsExceptionForNegativePrice() {
        assertThatThrownBy(() -> new Product("Apple", BigDecimal.valueOf(-10), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Product price cannot be null or negative");
    }
}
