package com.example.mymiaosha.controller;

import com.example.mymiaosha.controller.viewproject.ItemVO;
import com.example.mymiaosha.error.BusinessException;
import com.example.mymiaosha.error.EmBusinessError;
import com.example.mymiaosha.model.ItemModel;
import com.example.mymiaosha.response.CommonReturn;
import com.example.mymiaosha.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LL
 * @date 2021/9/2 16:47
 */
@Slf4j
@Controller
@RequestMapping("/item")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*",originPatterns = "*")
public class ItemController extends BaseController{

    @Autowired
    ItemService itemService;

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @RequestMapping(value = "/create",method = RequestMethod.POST,consumes = CONTENT_TYPE_FORMED)
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public CommonReturn createItem(@RequestParam("name")String name,
                                   @RequestParam("price")Double price,
                                   @RequestParam("stock")Integer stock,
                                   @RequestParam("description")String description,
                                   @RequestParam("imgUrl")String imgUrl) throws BusinessException {
        Boolean isLogin = (Boolean) redisTemplate.opsForValue().get("IS_LOGIN");

        if (!isLogin||isLogin==null) {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }

        ItemModel itemModel = new ItemModel();
        itemModel.setName(name);
        itemModel.setPrice(price);
        itemModel.setImgUrl(imgUrl);
        itemModel.setStock(stock);
        itemModel.setDescription(description);
        itemModel.setSales(0);

        itemService.createItem(itemModel);

        return CommonReturn.create(null);
    }

    @ResponseBody
    @RequestMapping("/list")
    public CommonReturn itemList() throws BusinessException {
        Boolean is_login = (Boolean) redisTemplate.opsForValue().get("IS_LOGIN");
        if (!is_login||is_login==null) {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        List<ItemModel> itemList = itemService.itemList();
        List<ItemVO> itemVOList = itemList.stream().map(itemModel -> {
            ItemVO itemVO = convertVOFromModel(itemModel);
            return itemVO;
        }).collect(Collectors.toList());
        return CommonReturn.create(itemVOList);
    }

    @ResponseBody
    @RequestMapping("/get")
    public CommonReturn getItem(@RequestParam("id")Integer id) throws BusinessException {
        Boolean isLogin = (Boolean) redisTemplate.opsForValue().get("IS_LOGIN");

        if (isLogin==null||!isLogin) {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        ItemModel itemModel = itemService.getItemById(id);
        if (itemModel==null) {
            throw new BusinessException(EmBusinessError.ITEM_NOT_EXIST);
        }
        ItemVO itemVO = convertVOFromModel(itemModel);
        return CommonReturn.create(itemVO);
    }

    private ItemVO convertVOFromModel(ItemModel itemModel){
        if (itemModel==null) {
            return null;
        }
        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel, itemVO);
        if (itemModel.getPromoModel()!=null) {
            itemVO.setPromoId(itemModel.getPromoModel().getId());
            itemVO.setPromoStatus(itemModel.getPromoModel().getStatus());
            itemVO.setPromoPrice(itemModel.getPromoModel().getPromoPrice());
            itemVO.setPromoStarTime(itemModel.getPromoModel().getPromoStarTime().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
        }else {
            itemVO.setPromoStatus(0);
        }
        return itemVO;
    }
}
