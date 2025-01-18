package com.andrei.supermarket;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OfferTest {

    @Test
    void offerConstructorThrowsExceptionForInvalidValues() {
        assertThatThrownBy(() -> new Offer(0, 45))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid offer configuration");

        assertThatThrownBy(() -> new Offer(2, -45))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid offer configuration");
    }
}
