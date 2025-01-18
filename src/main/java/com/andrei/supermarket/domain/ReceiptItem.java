package com.andrei.supermarket.domain;

import java.math.BigDecimal;

public record ReceiptItem(String productName, int quantity, BigDecimal price) {
}
