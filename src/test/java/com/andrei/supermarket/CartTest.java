package com.andrei.supermarket;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class CartTest {
    @Test
    void emptyCartHasTotalPriceOfZero() {
        Cart cart = new Cart();
        assertThat(cart.totalPrice()).isZero();
    }

    @Test
    void cartWithOneItemHasCorrectTotalPrice() {
        Cart cart = new Cart();
        cart.scanItem(new Product("Apple", 30, new Offer(2, 45)));
        assertThat(cart.totalPrice()).isEqualTo(30);
    }

    @Test
    void cartWithMultipleItemsHasCorrectTotalPrice() {
        Cart cart = new Cart();
        cart.scanItem(new Product("Apple", 30, new Offer(2, 45)));
        cart.scanItem(new Product("Banana", 50, new Offer(3, 130)));
        assertThat(cart.totalPrice()).isEqualTo(80);
    }

    @Test
    void cartAppliesDiscountForOfferItems() {
        Cart cart = new Cart();
        Product apple = new Product("apple", 30, new Offer(2, 45));
        cart.scanItem(apple);
        cart.scanItem(apple);
        assertThat(cart.totalPrice()).isEqualTo(45);
    }

    @Test
    void cartAppliesDiscountForOfferItemsAndNonOfferItems() {
        Cart cart = new Cart();
        Product apple = new Product("apple", 30, new Offer(2, 45));
        cart.scanItem(apple);
        cart.scanItem(apple);
        cart.scanItem(apple);
        cart.scanItem(new Product("No offer", 10, null));
        assertThat(cart.totalPrice()).isEqualTo(85);
    }

    @Test
    void cartThrowsExceptionForNullProduct() {
        Cart cart = new Cart();
        assertThatThrownBy(() -> cart.scanItem(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Product cannot be null");
    }
}
