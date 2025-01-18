package com.andrei.supermarket;

import org.springframework.stereotype.Service;

@Service
public class CartService {
    public void scanItem(String productName) {
        throw new IllegalArgumentException("Unknown productName: " + productName);
    }

    public int totalPrice() {
        return 0;
    }
}
