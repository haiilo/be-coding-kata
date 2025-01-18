package com.andrei.supermarket.domain;

import java.math.BigDecimal;
import java.util.List;

public record Receipt(List<ReceiptItem> items, BigDecimal total) {
}