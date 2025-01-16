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
        productsQuantities.put(product, productsQuantities.getOrDefault(product, 0) + 1);
    }

    private int getProductTotal(Product product, Integer quantity) {
        int offerTotal = (quantity / product.offer().quantity()) * product.offer().price();
        int outOfOfferTotal = (quantity % product.offer().quantity()) * product.price();
        return offerTotal + outOfOfferTotal;
    }
}
