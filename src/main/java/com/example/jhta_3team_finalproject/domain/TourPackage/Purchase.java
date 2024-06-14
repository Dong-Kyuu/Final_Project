package com.example.jhta_3team_finalproject.domain.TourPackage;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Purchase {

    private Integer id;
    private String impUid;
    private String merchantUid;
    private Integer buyerNo;
    private BigDecimal amount;
    private String status;
    private LocalDateTime createdAt = LocalDateTime.now();
    private Integer tripNo;

    private String customerNameKor;
    private String tripName;
    private String tripDate;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImpUid() {
        return impUid;
    }

    public void setImpUid(String impUid) {
        this.impUid = impUid;
    }

    public String getMerchantUid() {
        return merchantUid;
    }

    public void setMerchantUid(String merchantUid) {
        this.merchantUid = merchantUid;
    }

    public Integer getBuyerNo() {
        return buyerNo;
    }

    public void setBuyerNo(Integer buyerNo) {
        this.buyerNo = buyerNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getTripNo() {
        return tripNo;
    }

    public void setTripNo(Integer tripNo) {
        this.tripNo = tripNo;
    }

    public String getCustomerNameKor() {
        return customerNameKor;
    }

    public void setCustomerNameKor(String customerNameKor) {
        this.customerNameKor = customerNameKor;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }
}