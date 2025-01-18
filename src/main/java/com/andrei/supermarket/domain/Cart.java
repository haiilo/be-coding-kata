package com.andrei.supermarket.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart {
    private Map<Product, Integer> productsQuantities = new HashMap<>();

    public Receipt generateReceipt() {
        List<ReceiptItem> receiptItems = productsQuantities.entrySet().stream().map(
                (entry) -> {
                    Product product = entry.getKey();
                    int quantity = entry.getValue();
                    return new ReceiptItem(product.name(), quantity, getProductTotal(product, quantity));
                }).toList();
        return new Receipt(receiptItems, receiptItems.stream().mapToInt(ReceiptItem::price).sum());
    }

    public void scanItem(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        productsQuantities.put(product, productsQuantities.getOrDefault(product, 0) + 1);
    }

    private int getProductTotal(Product product, Integer quantity) {
        if (product.offers() != null && !product.offers().isEmpty()) {
            return product.offers().stream()
                    .mapToInt(offer -> calculateOfferPrice(offer, product.price(), quantity))
                    .min()
                    .orElse(quantity * product.price());
        }
        return quantity * product.price();
    }

    private int calculateOfferPrice(Offer offer, int unitPrice, int quantity) {
        int offerTotal = (quantity / offer.quantity()) * offer.price();
        int remainderTotal = (quantity % offer.quantity()) * unitPrice;
        return offerTotal + remainderTotal;
    }

    public void emptyCart() {
        productsQuantities = new HashMap<>();
    }
}
