package com.admedia.bendre.model.woocommerce;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    @JsonProperty("status")
    private String status;

    @JsonProperty("billing")
    private Billing billing;

    @JsonProperty("shipping")
    private Shipping shipping;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("payment_method_title")
    private String paymentMethodTitle;

    @JsonProperty("line_items")
    private List<OrderItem> orderItems;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Billing getBilling() {
        return billing;
    }

    public void setBilling(Billing billing) {
        this.billing = billing;
    }

    public Shipping getShipping() {
        return shipping;
    }

    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMethodTitle() {
        return paymentMethodTitle;
    }

    public void setPaymentMethodTitle(String paymentMethodTitle) {
        this.paymentMethodTitle = paymentMethodTitle;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> lineItems) {
        this.orderItems = lineItems;
    }
}
