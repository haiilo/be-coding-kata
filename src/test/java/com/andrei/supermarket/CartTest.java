package com.andrei.supermarket;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CartTest {

    @Test
    void emptyCartHasTotalPriceOfZero() {
        Cart cart = new Cart();
        assertThat(cart.totalPrice()).isZero();
    }
}
