package com.andrei.supermarket;

import com.andrei.supermarket.domain.Receipt;
import com.andrei.supermarket.domain.ReceiptItem;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class CartControllerTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    public CartControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void scanItemAddsProductToCart() {
        String productName = "Apple";

        String response = cartController.scanItem(productName);
        assertThat(response).isEqualTo("Apple scanned");
        verify(cartService, times(1)).scanItem(productName);
    }

    @Test
    void generateReceiptReturnsCorrectDetails() {
        Receipt receipt = new Receipt(
                List.of(new ReceiptItem("Apple", 2, 45),
                        new ReceiptItem("Banana", 1, 50)),
                95
        );

        when(cartService.getReceipt()).thenReturn(receipt);

        Receipt generatedReceipt = cartController.getReceipt();

        assertThat(generatedReceipt.items()).hasSize(2);
        assertThat(generatedReceipt.total()).isEqualTo(95);

        ReceiptItem appleItem = generatedReceipt.items().get(0);
        assertThat(appleItem.productName()).isEqualTo("Apple");
        assertThat(appleItem.quantity()).isEqualTo(2);
        assertThat(appleItem.price()).isEqualTo(45);

        ReceiptItem bananaItem = generatedReceipt.items().get(1);
        assertThat(bananaItem.productName()).isEqualTo("Banana");
        assertThat(bananaItem.quantity()).isEqualTo(1);
        assertThat(bananaItem.price()).isEqualTo(50);
    }

    @Test
    void generateReceiptHandlesEmptyCart() {
        Receipt receipt = new Receipt(List.of(), 0);

        when(cartService.getReceipt()).thenReturn(receipt);

        Receipt generatedReceipt = cartController.getReceipt();

        assertThat(generatedReceipt.items()).isEmpty();
        assertThat(generatedReceipt.total()).isZero();
    }
}
