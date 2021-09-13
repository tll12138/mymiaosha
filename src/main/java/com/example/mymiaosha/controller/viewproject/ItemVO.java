package com.example.mymiaosha.controller.viewproject;

import lombok.Data;

/**
 * @author LL
 * @date 2021/9/2 16:47
 */
@Data
public class ItemVO {

    private Integer id;

    private String name;

    private Double price;

    private Integer sales;

    private String imgUrl;

    private Integer stock;

    private String description;

    private Integer promoStatus;

    private Integer promoId;

    private Double promoPrice;

    private String promoStarTime;
}
