package com.example.mymiaosha.service;

import com.example.mymiaosha.error.BusinessException;
import com.example.mymiaosha.model.PromoModel;

/**
 * @author LL
 * @date 2021/9/13 15:45
 */
public interface PromoService {
    /**
     * 根据商品id查是否有相应的秒杀活动
     * @param id
     * @return
     */
    PromoModel getPromoByItemId(Integer id) throws BusinessException;
}
