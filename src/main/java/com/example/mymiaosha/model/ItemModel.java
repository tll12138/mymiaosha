package com.example.mymiaosha.model;

import lombok.Data;

/**
 * @author LL
 * @date 2021/9/2 14:46
 */
@Data
public class ItemModel {
    private Integer id;

    private String name;

    private Double price;

    private Integer sales;

    private String imgUrl;

    private Integer stock;

    private String description;

    private PromoModel promoModel;
}
