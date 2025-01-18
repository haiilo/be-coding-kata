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

import java.math.BigDecimal;
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
        ProductModel productModel = new ProductModel("Apple", BigDecimal.valueOf(30.00), List.of(new OfferModel(1L, 2, BigDecimal.valueOf(45.00))));
        when(productRepository.findById("Apple")).thenReturn(Optional.of(productModel));

        cartService.scanItem("Apple");

        verify(cart, times(1)).scanItem(new Product("Apple", BigDecimal.valueOf(30.00), List.of(new Offer(2, BigDecimal.valueOf(45.00)))));
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
                List.of(new ReceiptItem("Apple", 2, BigDecimal.valueOf(45.00)),
                        new ReceiptItem("Banana", 1, BigDecimal.valueOf(50.00))),
                BigDecimal.valueOf(95.00)
        );

        when(cart.generateReceipt()).thenReturn(receipt);

        Receipt generatedReceipt = cartService.getReceipt();

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

        when(cart.generateReceipt()).thenReturn(receipt);

        Receipt generatedReceipt = cartService.getReceipt();

        assertThat(generatedReceipt.items()).isEmpty();
        assertThat(generatedReceipt.total()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void testEmptyCart() {
        cartService.emptyCart();
        verify(cart).emptyCart();
    }
}
