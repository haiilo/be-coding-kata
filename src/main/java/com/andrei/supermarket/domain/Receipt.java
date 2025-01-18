package com.andrei.supermarket.domain;

import java.util.List;

public record Receipt(List<ReceiptItem> items, int total) {
}