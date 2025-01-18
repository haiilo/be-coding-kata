package com.andrei.supermarket.domain;

import java.math.BigDecimal;

public record Offer(int quantity, BigDecimal price) {
    public Offer {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Offer quantity must be greater than 0");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Offer price cannot be null or negative");
        }
    }
}
