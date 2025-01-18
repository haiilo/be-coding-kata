package com.andrei.supermarket.domain;

import java.math.BigDecimal;
import java.util.List;

public record Product(String name, BigDecimal price, List<Offer> offers) {
    public Product {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Product price cannot be null or negative");
        }
    }
}
