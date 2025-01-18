package com.andrei.supermarket;

import com.andrei.supermarket.domain.Cart;
import com.andrei.supermarket.domain.Offer;
import com.andrei.supermarket.domain.Product;
import com.andrei.supermarket.domain.Receipt;
import com.andrei.supermarket.model.ProductModel;
import com.andrei.supermarket.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CartService {
    // assuming we are working with a single cart,
    // API for demonstration purposes only
    private final Cart cart;
    private final ProductRepository productRepository;
    public void scanItem(String productName) {
        ProductModel productModel = productRepository.findById(productName).orElseThrow(
                () -> new IllegalArgumentException("Unknown productName: " + productName)
        );
        cart.scanItem(fromModel(productModel));
    }

    public Receipt getReceipt() {
        return cart.generateReceipt();
    }

    private Product fromModel(ProductModel productModel) {
        List<Offer> offers = productModel.getOffers().stream()
                .map((offerModel) -> new Offer(offerModel.getQuantity(), offerModel.getPrice()))
                .toList();
        return new Product(productModel.getName(), productModel.getPrice(), offers);
    }

    public void emptyCart() {
        cart.emptyCart();
    }
}
