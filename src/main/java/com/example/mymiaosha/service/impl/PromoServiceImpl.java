package com.example.mymiaosha.service.impl;

import com.example.mymiaosha.bean.Promo;
import com.example.mymiaosha.dao.PromoMapper;
import com.example.mymiaosha.error.BusinessException;
import com.example.mymiaosha.error.EmBusinessError;
import com.example.mymiaosha.model.PromoModel;
import com.example.mymiaosha.service.PromoService;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author LL
 * @date 2021/9/13 15:45
 */
@Service
public class PromoServiceImpl implements PromoService {

    @Autowired
    PromoMapper promoMapper;

    @Override
    public PromoModel getPromoByItemId(Integer id) throws BusinessException {
        if (id==null){
            throw new BusinessException(EmBusinessError.PARAMETER_NOT_LEGITIMATE);
        }
        Promo promo = promoMapper.selectByItemId(id);
        PromoModel promoModel = convertModelFromPromo(promo);
        if (promoModel==null){
            return null;
        }
        if (promoModel.getPromoStarTime().isAfterNow()){
            promoModel.setStatus(0);
        }else if (promoModel.getPromoEndTime().isBeforeNow()){
            promoModel.setStatus(2);
        }else {
            promoModel.setStatus(1);
        }
        return promoModel;
    }

    private PromoModel convertModelFromPromo(Promo promo){
        if (promo==null){
            return null;
        }
        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promo, promoModel);
        promoModel.setPromoStarTime(new DateTime(promo.getPromoStartTime()));
        promoModel.setPromoEndTime(new DateTime(promo.getPromoEndTime()));
        return promoModel;
    }
}
