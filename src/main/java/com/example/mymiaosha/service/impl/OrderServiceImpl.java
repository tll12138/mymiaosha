package com.example.mymiaosha.service.impl;

import com.example.mymiaosha.bean.Item;
import com.example.mymiaosha.bean.ItemStock;
import com.example.mymiaosha.bean.OrderInfo;
import com.example.mymiaosha.dao.ItemMapper;
import com.example.mymiaosha.dao.ItemStockMapper;
import com.example.mymiaosha.dao.OrderInfoMapper;
import com.example.mymiaosha.error.BusinessException;
import com.example.mymiaosha.error.EmBusinessError;
import com.example.mymiaosha.model.ItemModel;
import com.example.mymiaosha.model.OrderModel;
import com.example.mymiaosha.model.PromoModel;
import com.example.mymiaosha.model.UserModel;
import com.example.mymiaosha.service.ItemService;
import com.example.mymiaosha.service.OrderService;
import com.example.mymiaosha.service.PromoService;
import com.example.mymiaosha.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author LL
 * @date 2021/9/10 15:47
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemMapper itemMapper;

    @Autowired
    ItemStockMapper itemStockMapper;

    @Autowired
    UserService userService;

    @Autowired
    OrderInfoMapper orderInfoMapper;

    @Autowired
    PromoService promoService;

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @Override
    public void createOrder(Integer itemId,Integer amount) throws BusinessException {
        if (itemId==null){
            throw new BusinessException(EmBusinessError.PARAMETER_NOT_LEGITIMATE);
        }
        if (amount==null||amount==0){
            throw new BusinessException(EmBusinessError.PARAMETER_NOT_LEGITIMATE);
        }
        ItemModel item = itemService.getItemById(itemId);
        UserModel user = (UserModel) redisTemplate.opsForValue().get("user");
        ItemStock itemStock = itemStockMapper.selectByItemId(itemId);
        if (user==null){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        if (itemStock.getStock()<amount||itemStock.getStock()==0){
            throw new BusinessException(EmBusinessError.ITEM_NOT_ENOUGH);
        }


        OrderModel orderModel = new OrderModel();
        Date now = new Date();
        SimpleDateFormat orderTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        PromoModel promoModel = promoService.getPromoByItemId(itemId);
        if (promoModel!=null&&promoModel.getPromoStarTime().isBeforeNow()&&promoModel.getPromoEndTime().isAfterNow()){
            orderModel.setItemPrice(promoModel.getPromoPrice());
        }else {
            orderModel.setItemPrice(item.getPrice());
        }

        orderModel.setId(getOrderId(now));
        orderModel.setUserId(user.getId());
        orderModel.setItemId(itemId);
        orderModel.setAmount(amount);
        orderModel.setOrderPrice(orderModel.getItemPrice()*orderModel.getAmount());
        orderModel.setOrderTime(orderTime.format(now));

        OrderInfo orderInfo = convertOrderFromModel(orderModel);

        item.setSales(item.getSales()+amount);
        itemStock.setStock(itemStock.getStock()-amount);

        orderInfoMapper.insertSelective(orderInfo);
        itemMapper.updateByPrimaryKeySelective(convertItemFromModel(item));
        itemStockMapper.updateByItemId(itemStock);

    }

    private OrderInfo convertOrderFromModel(OrderModel orderModel){
        if (orderModel==null){
            return null;
        }
        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(orderModel, orderInfo);
        return orderInfo;
    }

    private Item convertItemFromModel(ItemModel itemModel){
        if (itemModel==null){
            return null;
        }
        Item item = new Item();
        BeanUtils.copyProperties(itemModel, item);
        return item;
    }

    private String getOrderId(Date now){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");
        String date = dateFormat.format(now);
        Random random = new Random();
        int rand = random.nextInt(89999);
        rand+=10000;
        date+=rand;
        return date;
    }
}
