package com.admedia.bendre.model.woocommerce;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class OrderItem implements Serializable {
    @JsonProperty("product_id")
    private Long productId;

    @JsonProperty("quantity")
    private int quantity;

    public OrderItem(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
