package com.andrei.supermarket;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

        cartController.scanItem(productName);
        verify(cartService, times(1)).scanItem(productName);
    }

    @Test
    void getTotalPriceReturnsCorrectTotal() {
        when(cartService.totalPrice()).thenReturn(100);
        int totalPrice = cartController.getTotalPrice();
        assertThat(totalPrice).isEqualTo(100);
        verify(cartService, times(1)).totalPrice();
    }
}
