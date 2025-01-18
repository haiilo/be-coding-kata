package com.andrei.supermarket;

public record Offer(int quantity, int price) {
    public Offer {
        if (quantity <= 0 || price < 0) {
            throw new IllegalArgumentException("Invalid offer configuration");
        }
    }
}
