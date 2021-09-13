package com.example.mymiaosha.service;

import com.example.mymiaosha.error.BusinessException;

/**
 * @author LL
 * @date 2021/9/10 15:47
 */
public interface OrderService {
    /**
     * 创建一个订单
     * @param itemId
     * @param amount
     * @throws BusinessException
     */
    void createOrder(Integer itemId,Integer amount) throws BusinessException;
}
