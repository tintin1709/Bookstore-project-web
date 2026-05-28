package com.example.bookstore.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderSummary {
    private Long id;
    private String orderNo;
    private String customerName;
    private String status;
    private String paymentStatus;
    private String shipmentStatus;
    private BigDecimal grandTotal;
    private LocalDateTime placedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public String getShipmentStatus() { return shipmentStatus; }
    public void setShipmentStatus(String shipmentStatus) { this.shipmentStatus = shipmentStatus; }
    public BigDecimal getGrandTotal() { return grandTotal; }
    public void setGrandTotal(BigDecimal grandTotal) { this.grandTotal = grandTotal; }
    public LocalDateTime getPlacedAt() { return placedAt; }
    public void setPlacedAt(LocalDateTime placedAt) { this.placedAt = placedAt; }
}
