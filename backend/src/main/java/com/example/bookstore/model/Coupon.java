package com.example.bookstore.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Coupon {
    private Long id;
    private String code;
    private String discountType;
    private BigDecimal discountValue;
    private BigDecimal minOrderAmount;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
    private String status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDiscountType() { return discountType; }
    public void setDiscountType(String discountType) { this.discountType = discountType; }
    public BigDecimal getDiscountValue() { return discountValue; }
    public void setDiscountValue(BigDecimal discountValue) { this.discountValue = discountValue; }
    public BigDecimal getMinOrderAmount() { return minOrderAmount; }
    public void setMinOrderAmount(BigDecimal minOrderAmount) { this.minOrderAmount = minOrderAmount; }
    public LocalDateTime getStartsAt() { return startsAt; }
    public void setStartsAt(LocalDateTime startsAt) { this.startsAt = startsAt; }
    public LocalDateTime getEndsAt() { return endsAt; }
    public void setEndsAt(LocalDateTime endsAt) { this.endsAt = endsAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
