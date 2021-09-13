package com.example.mymiaosha.bean;

import java.util.Date;

public class Promo {
    private Integer id;

    private String promoName;

    private Integer itemId;

    private Double promoPrice;

    private Date promoStartTime;

    private Date promoEndTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPromoName() {
        return promoName;
    }

    public void setPromoName(String promoName) {
        this.promoName = promoName == null ? null : promoName.trim();
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Double getPromoPrice() {
        return promoPrice;
    }

    public void setPromoPrice(Double promoPrice) {
        this.promoPrice = promoPrice;
    }

    public Date getPromoStartTime() {
        return promoStartTime;
    }

    public void setPromoStartTime(Date promoStartTime) {
        this.promoStartTime = promoStartTime;
    }

    public Date getPromoEndTime() {
        return promoEndTime;
    }

    public void setPromoEndTime(Date promoEndTime) {
        this.promoEndTime = promoEndTime;
    }
}