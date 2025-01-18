package com.andrei.supermarket;

import com.andrei.supermarket.domain.Cart;
import com.andrei.supermarket.domain.Offer;
import com.andrei.supermarket.domain.Product;
import com.andrei.supermarket.domain.Receipt;
import com.andrei.supermarket.domain.ReceiptItem;
import com.andrei.supermarket.model.OfferModel;
import com.andrei.supermarket.model.ProductModel;
import com.andrei.supermarket.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
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
        ProductModel productModel = new ProductModel("Apple", 30, new OfferModel(1L, 2, 45));
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
    void generateReceiptReturnsCorrectDetails() {
        Receipt receipt = new Receipt(
                List.of(new ReceiptItem("Apple", 2, 45),
                        new ReceiptItem("Banana", 1, 50)),
                95
        );

        when(cart.generateReceipt()).thenReturn(receipt);

        Receipt generatedReceipt = cartService.getReceipt();

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

        when(cart.generateReceipt()).thenReturn(receipt);

        Receipt generatedReceipt = cartService.getReceipt();

        assertThat(generatedReceipt.items()).isEmpty();
        assertThat(generatedReceipt.total()).isZero();
    }
}
