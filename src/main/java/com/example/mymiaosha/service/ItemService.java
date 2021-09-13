package com.example.mymiaosha.service;

import com.example.mymiaosha.error.BusinessException;
import com.example.mymiaosha.model.ItemModel;

import java.util.List;

/**
 * @author LL
 * @date 2021/9/2 14:56
 */
public interface ItemService {

    /**
     * 创建一个新的商品条目
     */
    void createItem(ItemModel itemModel) throws BusinessException;

    /**
     * 查找一个item的集合
     * @return
     */
    List<ItemModel> itemList() throws BusinessException;

    /**
     * 根据商品id查找商品详情
     * @return
     */
    ItemModel getItemById(Integer id) throws BusinessException;
}
