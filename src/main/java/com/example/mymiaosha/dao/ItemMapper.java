package com.example.mymiaosha.dao;

import com.example.mymiaosha.bean.Item;

import java.util.List;

public interface ItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Item record);

    int insertSelective(Item record);

    Item selectByPrimaryKey(Integer id);

    List<Item> selectItems();

    int updateByPrimaryKeySelective(Item record);

    int updateByPrimaryKey(Item record);
}