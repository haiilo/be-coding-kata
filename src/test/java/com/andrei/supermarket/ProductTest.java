package com.andrei.supermarket;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProductTest {

    @Test
    void productConstructorThrowsExceptionForInvalidName() {
        assertThatThrownBy(() -> new Product(null, 30, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Product name cannot be null or empty");

        assertThatThrownBy(() -> new Product("", 30, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Product name cannot be null or empty");
    }

    @Test
    void productConstructorThrowsExceptionForNegativePrice() {
        assertThatThrownBy(() -> new Product("Apple", -10, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Product price cannot be negative");
    }
}
