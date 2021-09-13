package com.example.mymiaosha.model;

import lombok.Data;

/**
 * @author LL
 * @date 2021/9/10 15:11
 */
@Data
public class OrderModel {
    
    private String id;

    private Integer userId;

    private Integer itemId;

    private Double itemPrice;

    private Integer amount;

    private Double orderPrice;

    private String orderTime;
}
