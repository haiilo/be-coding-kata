package com.andrei.supermarket;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CartTest {

    @Test
    void emptyCartHasTotalPriceOfZero() {
        Cart cart = new Cart();
        assertThat(cart.totalPrice()).isZero();
    }

    @Test
    void cartWithOneItemHasCorrectTotalPrice() {
        Cart cart = new Cart();
        cart.scanItem("Apple", 30);
        assertThat(cart.totalPrice()).isEqualTo(30);
    }

    @Test
    void cartWithMultipleItemsHasCorrectTotalPrice() {
        Cart cart = new Cart();
        cart.scanItem("Apple", 30);
        cart.scanItem("Banana", 50);
        assertThat(cart.totalPrice()).isEqualTo(80);
    }
}
