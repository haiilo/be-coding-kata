package com.andrei.supermarket;

import com.andrei.supermarket.domain.Receipt;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    @Operation(summary = "Scan an item into the cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item scanned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid product name",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })

    @PostMapping("/scan")
    @ResponseStatus(HttpStatus.OK)
    public String scanItem(String productName) {
        cartService.scanItem(productName);
        return String.format("%s scanned", productName);
    }

    @Operation(summary = "Get the receipt containng the products in the cart with the applied discounts and the total")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Total price retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/receipt")
    @ResponseStatus(HttpStatus.OK)
    public Receipt getReceipt() {
        return cartService.getReceipt();
    }

    @PostMapping("/empty")
    @ResponseStatus(HttpStatus.OK)
    public String emptyCart() {
        cartService.emptyCart();
        return "Emptied cart";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException e) {
        return e.getMessage();
    }
}
