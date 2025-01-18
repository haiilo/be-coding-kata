package com.andrei.supermarket;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    private final Map<Product, Integer> productsQuantities = new HashMap<>();
    public int totalPrice() {
        return productsQuantities.entrySet().stream()
                .mapToInt((entry) -> getProductTotal(entry.getKey(), entry.getValue())).sum();
    }

    public void scanItem(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        productsQuantities.put(product, productsQuantities.getOrDefault(product, 0) + 1);
    }

    private int getProductTotal(Product product, Integer quantity) {
        if (product.offer() != null) {
            int offerTotal = (quantity / product.offer().quantity()) * product.offer().price();
            int outOfOfferTotal = (quantity % product.offer().quantity()) * product.price();
            return offerTotal + outOfOfferTotal;
        } else {
            return quantity * product.price();
        }
    }
}
