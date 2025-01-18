package com.andrei.supermarket.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OfferTest {

    @Test
    void offerConstructorThrowsExceptionForInvalidValues() {
        assertThatThrownBy(() -> new Offer(0, BigDecimal.valueOf(45.00)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid offer configuration");

        assertThatThrownBy(() -> new Offer(2, BigDecimal.valueOf(-45.00)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid offer configuration");
    }
}
