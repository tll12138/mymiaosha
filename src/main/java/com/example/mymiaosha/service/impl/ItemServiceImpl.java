package com.example.mymiaosha.service.impl;

import com.example.mymiaosha.bean.Item;
import com.example.mymiaosha.bean.ItemStock;
import com.example.mymiaosha.dao.ItemMapper;
import com.example.mymiaosha.dao.ItemStockMapper;
import com.example.mymiaosha.error.BusinessException;
import com.example.mymiaosha.error.EmBusinessError;
import com.example.mymiaosha.model.ItemModel;
import com.example.mymiaosha.model.PromoModel;
import com.example.mymiaosha.service.ItemService;
import com.example.mymiaosha.service.PromoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LL
 * @date 2021/9/2 14:56
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    ItemMapper itemMapper;

    @Autowired
    ItemStockMapper itemStockMapper;

    @Autowired
    PromoService promoService;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;


    @Override
    public void createItem(ItemModel itemModel) throws BusinessException {

        if (StringUtils.isEmpty(itemModel.getName()) ||
                itemModel.getPrice() <= 0 ||
                itemModel.getStock() <= 0 ||
                StringUtils.isEmpty(itemModel.getDescription()) ||
                StringUtils.isEmpty(itemModel.getImgUrl())) {
            throw new BusinessException(EmBusinessError.PARAMETER_NOT_LEGITIMATE);
        }

        Item item = convertItemFromModel(itemModel);
        itemMapper.insertSelective(item);
        itemModel.setId(item.getId());
        ItemStock itemStock = convertStockFromModel(itemModel);
        itemStockMapper.insertSelective(itemStock);
    }

    @Override
    public List<ItemModel> itemList() throws BusinessException {

        List<Item> items;
        Boolean isLogin = (Boolean) redisTemplate.opsForValue().get("IS_LOGIN");
        if (isLogin==null||!isLogin) {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        items = itemMapper.selectItems();
        List<ItemModel> itemModelList = items.stream().map(item -> {
            ItemStock itemStock = itemStockMapper.selectByItemId(item.getId());
            ItemModel itemModel = convertModelFromItemAndStock(item, itemStock);
            return itemModel;
        }).collect(Collectors.toList());

        return itemModelList;
    }

    @Override
    public ItemModel getItemById(Integer id) throws BusinessException {

        Item item = itemMapper.selectByPrimaryKey(id);
        if (item==null) {
            throw new BusinessException(EmBusinessError.ITEM_NOT_EXIST);
        }
        ItemStock itemStock = itemStockMapper.selectByItemId(id);

        ItemModel itemModel = convertModelFromItemAndStock(item, itemStock);
        PromoModel promoModel = promoService.getPromoByItemId(id);
        if (promoModel!=null||promoModel.getStatus()!=2){
            itemModel.setPromoModel(promoModel);
        }
        return itemModel;
    }

    private Item convertItemFromModel(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        }
        Item item = new Item();
        BeanUtils.copyProperties(itemModel, item);
        item.setImgurl(itemModel.getImgUrl());
        return item;
    }

    private ItemStock convertStockFromModel(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        }
        ItemStock itemStock = new ItemStock();
        itemStock.setStock(itemModel.getStock());
        itemStock.setItemId(itemModel.getId());
        return itemStock;
    }

    private ItemModel convertModelFromItemAndStock(Item item, ItemStock itemStock) {
        if (item == null || itemStock == null) {
            return null;
        }
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(item, itemModel);
        itemModel.setImgUrl(item.getImgurl());
        itemModel.setStock(itemStock.getStock());
        return itemModel;
    }


}
