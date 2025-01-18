package com.andrei.supermarket.domain;

import java.math.BigDecimal;
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
        return new Receipt(receiptItems, receiptItems.stream().map(ReceiptItem::price).reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    public void scanItem(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        productsQuantities.put(product, productsQuantities.getOrDefault(product, 0) + 1);
    }

    private BigDecimal getProductTotal(Product product, Integer quantity) {
        if (product.offers() != null && !product.offers().isEmpty()) {
            return product.offers().stream()
                    .map(offer -> calculateOfferPrice(offer, product.price(), quantity))
                    .min(BigDecimal::compareTo)
                    .orElse(product.price().multiply(BigDecimal.valueOf(quantity)));
        }
        return product.price().multiply(BigDecimal.valueOf(quantity));
    }

    private BigDecimal calculateOfferPrice(Offer offer, BigDecimal unitPrice, int quantity) {
        BigDecimal offerTotal = BigDecimal.valueOf(quantity / offer.quantity()).multiply(offer.price());
        BigDecimal remainderTotal = BigDecimal.valueOf(quantity % offer.quantity()).multiply(unitPrice);
        return offerTotal.add(remainderTotal);
    }

    public void emptyCart() {
        productsQuantities = new HashMap<>();
    }
}
