package com.example.mymiaosha.dao;

import com.example.mymiaosha.bean.Promo;

public interface PromoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Promo record);

    int insertSelective(Promo record);

    Promo selectByPrimaryKey(Integer id);

    Promo selectByItemId(Integer id);

    int updateByPrimaryKeySelective(Promo record);

    int updateByPrimaryKey(Promo record);
}