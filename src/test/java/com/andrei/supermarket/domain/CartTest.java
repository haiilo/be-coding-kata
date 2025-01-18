package com.andrei.supermarket.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class CartTest {
    @Test
    void emptyCartGeneratesEmptyReceipt() {
        Cart cart = new Cart();
        Receipt receipt = cart.generateReceipt();

        assertThat(receipt.items()).isEmpty();
        assertThat(receipt.total()).isZero();
    }

    @Test
    void cartWithOneItemGeneratesCorrectReceipt() {
        Cart cart = new Cart();
        Product apple = new Product("Apple", 30, List.of(new Offer(2, 45)));
        cart.scanItem(apple);

        Receipt receipt = cart.generateReceipt();

        assertThat(receipt.items()).hasSize(1);
        assertThat(receipt.items().get(0).productName()).isEqualTo("Apple");
        assertThat(receipt.items().get(0).quantity()).isEqualTo(1);
        assertThat(receipt.items().get(0).price()).isEqualTo(30);
        assertThat(receipt.total()).isEqualTo(30);
    }

    @Test
    void cartWithMultipleItemsGeneratesCorrectReceipt() {
        Cart cart = new Cart();
        cart.scanItem(new Product("Apple", 30, List.of(new Offer(2, 45))));
        cart.scanItem(new Product("Banana", 50, List.of(new Offer(3, 130))));

        Receipt receipt = cart.generateReceipt();

        assertThat(receipt.items()).hasSize(2);
        assertThat(receipt.total()).isEqualTo(80);
    }

    @Test
    void cartAppliesDiscountForOfferItemsInReceipt() {
        Cart cart = new Cart();
        Product apple = new Product("Apple", 30, List.of(new Offer(2, 45)));
        cart.scanItem(apple);
        cart.scanItem(apple);

        Receipt receipt = cart.generateReceipt();

        assertThat(receipt.items()).hasSize(1);
        assertThat(receipt.items().get(0).productName()).isEqualTo("Apple");
        assertThat(receipt.items().get(0).quantity()).isEqualTo(2);
        assertThat(receipt.items().get(0).price()).isEqualTo(45);
        assertThat(receipt.total()).isEqualTo(45);
    }

    @Test
    void cartAppliesDiscountAndNonOfferItemsInReceipt() {
        Cart cart = new Cart();
        Product apple = new Product("Apple", 30, List.of(new Offer(2, 45)));
        cart.scanItem(apple);
        cart.scanItem(apple);
        cart.scanItem(new Product("No offer", 10, null));

        Receipt receipt = cart.generateReceipt();

        assertThat(receipt.items()).hasSize(2);
        assertThat(receipt.total()).isEqualTo(55);
    }

    @Test
    void cartSelectsBestOfferForProduct() {
        Cart cart = new Cart();
        Product apple = new Product("Apple", 30, List.of(
                new Offer(2, 45),
                new Offer(5, 100)
        ));
        cart.scanItem(apple);
        cart.scanItem(apple);
        cart.scanItem(apple);
        cart.scanItem(apple);
        cart.scanItem(apple);

        Receipt receipt = cart.generateReceipt();

        assertThat(receipt.items()).hasSize(1);
        ReceiptItem appleItem = receipt.items().get(0);
        assertThat(appleItem.productName()).isEqualTo("Apple");
        assertThat(appleItem.quantity()).isEqualTo(5);
        assertThat(appleItem.price()).isEqualTo(100);
    }


    @Test
    void cartThrowsExceptionForNullProduct() {
        Cart cart = new Cart();
        assertThatThrownBy(() -> cart.scanItem(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Product cannot be null");
    }
}
