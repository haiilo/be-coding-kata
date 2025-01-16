package com.andrei.supermarket;

public class Cart {
    private int totalPrice = 0;
    public int totalPrice() {
        return totalPrice;
    }

    public void scanItem(String apple, int price) {
        this.totalPrice += price;
    }
}
