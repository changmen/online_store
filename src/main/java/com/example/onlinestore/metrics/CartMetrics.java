package com.example.onlinestore.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class CartMetrics {
    private final Counter addToCartCounter;
    private final Counter updateCartCounter;
    private final Counter deleteCartCounter;
    private final Counter clearCartCounter;

    public CartMetrics(MeterRegistry registry) {
        this.addToCartCounter = Counter.builder("cart.operations")
                .tag("operation", "add")
                .description("Number of add to cart operations")
                .register(registry);

        this.updateCartCounter = Counter.builder("cart.operations")
                .tag("operation", "update")
                .description("Number of update cart operations")
                .register(registry);

        this.deleteCartCounter = Counter.builder("cart.operations")
                .tag("operation", "delete")
                .description("Number of delete cart operations")
                .register(registry);

        this.clearCartCounter = Counter.builder("cart.operations")
                .tag("operation", "clear")
                .description("Number of clear cart operations")
                .register(registry);
    }

    public void incrementAddToCart() {
        addToCartCounter.increment();
    }

    public void incrementUpdateCart() {
        updateCartCounter.increment();
    }

    public void incrementDeleteCart() {
        deleteCartCounter.increment();
    }

    public void incrementClearCart() {
        clearCartCounter.increment();
    }
} 