package com.example.mymiaosha.dao;

import com.example.mymiaosha.bean.ItemStock;

public interface ItemStockMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ItemStock record);

    int insertSelective(ItemStock record);

    ItemStock selectByPrimaryKey(Integer id);

    ItemStock selectByItemId(Integer id);

    int updateByPrimaryKeySelective(ItemStock record);

    int updateByPrimaryKey(ItemStock record);

    int updateByItemId(ItemStock record);
}