package com.andrei.supermarket;

import com.andrei.supermarket.domain.Cart;
import com.andrei.supermarket.domain.Offer;
import com.andrei.supermarket.domain.Product;
import com.andrei.supermarket.model.ProductModel;
import com.andrei.supermarket.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public int totalPrice() {
        return cart.totalPrice();
    }

    private Product fromModel(ProductModel productModel) {
        Offer offer = new Offer(productModel.getOffer().getQuantity(), productModel.getOffer().getPrice());
        return new Product(productModel.getName(), productModel.getPrice(), offer);
    }
}
