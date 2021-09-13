package com.example.mymiaosha.model;

import lombok.Data;
import org.joda.time.DateTime;

/**
 * @author LL
 * @date 2021/9/11 16:08
 */
@Data
public class PromoModel {
    private Integer id;

    /**
     * 表示秒杀活动状态，0为未开始，1为已开始，2为已结束
     */
    private Integer status;

    private Integer itemId;

    private String promoName;

    private Double promoPrice;

    private DateTime promoStarTime;

    private DateTime promoEndTime;
}
