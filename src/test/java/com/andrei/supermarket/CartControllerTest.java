package com.andrei.supermarket;

import com.andrei.supermarket.domain.Receipt;
import com.andrei.supermarket.domain.ReceiptItem;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
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
                List.of(new ReceiptItem("Apple", 2, BigDecimal.valueOf(45.00)),
                        new ReceiptItem("Banana", 1, BigDecimal.valueOf(50.00))),
                BigDecimal.valueOf(95.00)
        );

        when(cartService.getReceipt()).thenReturn(receipt);

        Receipt generatedReceipt = cartController.getReceipt();

        assertThat(generatedReceipt.items()).hasSize(2);
        assertThat(generatedReceipt.total()).isEqualByComparingTo(BigDecimal.valueOf(95.00));

        ReceiptItem appleItem = generatedReceipt.items().getFirst();
        assertThat(appleItem.productName()).isEqualTo("Apple");
        assertThat(appleItem.quantity()).isEqualTo(2);
        assertThat(appleItem.price()).isEqualByComparingTo(BigDecimal.valueOf(45.00));

        ReceiptItem bananaItem = generatedReceipt.items().get(1);
        assertThat(bananaItem.productName()).isEqualTo("Banana");
        assertThat(bananaItem.quantity()).isEqualTo(1);
        assertThat(bananaItem.price()).isEqualByComparingTo(BigDecimal.valueOf(50.00));
    }

    @Test
    void generateReceiptHandlesEmptyCart() {
        Receipt receipt = new Receipt(List.of(), BigDecimal.ZERO);

        when(cartService.getReceipt()).thenReturn(receipt);

        Receipt generatedReceipt = cartController.getReceipt();

        assertThat(generatedReceipt.items()).isEmpty();
        assertThat(generatedReceipt.total()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void testEmptyCart() {
        String response = cartController.emptyCart();
        assertThat(response).isEqualTo("Emptied cart");
        verify(cartService, times(1)).emptyCart();
    }
}
