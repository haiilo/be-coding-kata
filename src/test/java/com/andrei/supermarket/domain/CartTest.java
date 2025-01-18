package com.andrei.supermarket.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CartTest {
    @Test
    void emptyCartGeneratesEmptyReceipt() {
        Cart cart = new Cart();
        Receipt receipt = cart.generateReceipt();

        assertThat(receipt.items()).isEmpty();
        assertThat(receipt.total()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void cartWithOneItemGeneratesCorrectReceipt() {
        Cart cart = new Cart();
        Product apple = new Product("Apple", BigDecimal.valueOf(30.00), List.of(new Offer(2, BigDecimal.valueOf(45.00))));
        cart.scanItem(apple);

        Receipt receipt = cart.generateReceipt();

        assertThat(receipt.items()).hasSize(1);
        assertThat(receipt.items().getFirst().productName()).isEqualTo("Apple");
        assertThat(receipt.items().getFirst().quantity()).isEqualTo(1);
        assertThat(receipt.items().getFirst().price()).isEqualByComparingTo(BigDecimal.valueOf(30.00));
        assertThat(receipt.total()).isEqualByComparingTo(BigDecimal.valueOf(30.00));
    }

    @Test
    void cartWithMultipleItemsGeneratesCorrectReceipt() {
        Cart cart = new Cart();
        cart.scanItem(new Product("Apple", BigDecimal.valueOf(30.00), List.of(new Offer(2, BigDecimal.valueOf(45.00)))));
        cart.scanItem(new Product("Banana", BigDecimal.valueOf(50.00), List.of(new Offer(3, BigDecimal.valueOf(130.00)))));

        Receipt receipt = cart.generateReceipt();

        assertThat(receipt.items()).hasSize(2);
        assertThat(receipt.total()).isEqualByComparingTo(BigDecimal.valueOf(80.00));
    }

    @Test
    void cartAppliesDiscountForOfferItemsInReceipt() {
        Cart cart = new Cart();
        Product apple = new Product("Apple", BigDecimal.valueOf(30.00), List.of(new Offer(2, BigDecimal.valueOf(45.00))));
        cart.scanItem(apple);
        cart.scanItem(apple);

        Receipt receipt = cart.generateReceipt();

        assertThat(receipt.items()).hasSize(1);
        assertThat(receipt.items().getFirst().productName()).isEqualTo("Apple");
        assertThat(receipt.items().getFirst().quantity()).isEqualTo(2);
        assertThat(receipt.items().getFirst().price()).isEqualByComparingTo(BigDecimal.valueOf(45.00));
        assertThat(receipt.total()).isEqualByComparingTo(BigDecimal.valueOf(45.00));
    }

    @Test
    void cartAppliesDiscountAndNonOfferItemsInReceipt() {
        Cart cart = new Cart();
        Product apple = new Product("Apple", BigDecimal.valueOf(30.00), List.of(new Offer(2, BigDecimal.valueOf(45.00))));
        cart.scanItem(apple);
        cart.scanItem(apple);
        cart.scanItem(new Product("No offer", BigDecimal.valueOf(10.00), null));

        Receipt receipt = cart.generateReceipt();

        assertThat(receipt.items()).hasSize(2);
        assertThat(receipt.total()).isEqualByComparingTo(BigDecimal.valueOf(55.00));
    }

    @Test
    void cartSelectsBestOfferForProduct() {
        Cart cart = new Cart();
        Product apple = new Product("Apple", BigDecimal.valueOf(30.00), List.of(
                new Offer(2, BigDecimal.valueOf(45.00)),
                new Offer(5, BigDecimal.valueOf(100.00))
        ));
        cart.scanItem(apple);
        cart.scanItem(apple);
        cart.scanItem(apple);
        cart.scanItem(apple);
        cart.scanItem(apple);

        Receipt receipt = cart.generateReceipt();

        assertThat(receipt.items()).hasSize(1);
        ReceiptItem appleItem = receipt.items().getFirst();
        assertThat(appleItem.productName()).isEqualTo("Apple");
        assertThat(appleItem.quantity()).isEqualTo(5);
        assertThat(appleItem.price()).isEqualByComparingTo(BigDecimal.valueOf(100.00));
    }

    @Test
    void cartEmptyCartWithItemsGeneratesEmptyReceipt() {
        Cart cart = new Cart();
        Product apple = new Product("Apple", BigDecimal.valueOf(30.00), List.of(
                new Offer(2, BigDecimal.valueOf(45.00))
        ));
        cart.scanItem(apple);
        cart.emptyCart();
        Receipt receipt = cart.generateReceipt();

        assertThat(receipt.items()).isEmpty();
        assertThat(receipt.total()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void cartThrowsExceptionForNullProduct() {
        Cart cart = new Cart();
        assertThatThrownBy(() -> cart.scanItem(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Product cannot be null");
    }

    @Test
    void cartHandlesFractionalPricesCorrectly() {
        Cart cart = new Cart();
        Product orange = new Product("Orange", BigDecimal.valueOf(1.25), List.of(
                new Offer(3, BigDecimal.valueOf(3.00))
        ));

        cart.scanItem(orange);
        cart.scanItem(orange);
        cart.scanItem(orange);
        cart.scanItem(orange);

        Receipt receipt = cart.generateReceipt();

        assertThat(receipt.items()).hasSize(1);
        ReceiptItem orangeItem = receipt.items().getFirst();
        assertThat(orangeItem.productName()).isEqualTo("Orange");
        assertThat(orangeItem.quantity()).isEqualTo(4);
        assertThat(orangeItem.price()).isEqualByComparingTo(BigDecimal.valueOf(4.25));
    }
}
