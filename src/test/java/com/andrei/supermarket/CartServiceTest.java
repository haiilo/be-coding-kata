package com.andrei.supermarket;

import com.andrei.supermarket.domain.Cart;
import com.andrei.supermarket.domain.Offer;
import com.andrei.supermarket.domain.Product;
import com.andrei.supermarket.model.OfferModel;
import com.andrei.supermarket.model.ProductModel;
import com.andrei.supermarket.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @Mock
    private Cart cart;

    @Mock
    private ProductRepository productRepository;

    private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cartService = new CartService(cart, productRepository);
    }

    @Test
    void scanItemAddsProductToCart() {
        ProductModel productModel = new ProductModel("Apple", 30, new OfferModel(1L,2, 45));
        when(productRepository.findById("Apple")).thenReturn(Optional.of(productModel));

        cartService.scanItem("Apple");

        verify(cart, times(1)).scanItem(new Product("Apple", 30, new Offer(2, 45)));
    }

    @Test
    void scanItemThrowsExceptionForUnknownProduct() {
        when(productRepository.findById("Unknown")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.scanItem("Unknown"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unknown productName: Unknown");

        verify(cart, never()).scanItem(any(Product.class));
    }

    @Test
    void totalPriceReturnsCorrectValue() {
        when(cart.totalPrice()).thenReturn(150);

        int total = cartService.totalPrice();

        assertThat(total).isEqualTo(150);
        verify(cart, times(1)).totalPrice();
    }
}
