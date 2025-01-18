package com.andrei.supermarket.domain;

import java.util.List;

public record Product(String name, int price, List<Offer> offers) {
    public Product {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Product price cannot be negative");
        }
    }
}
