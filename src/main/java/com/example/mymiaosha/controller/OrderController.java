package com.example.mymiaosha.controller;

import com.example.mymiaosha.error.BusinessException;
import com.example.mymiaosha.error.EmBusinessError;
import com.example.mymiaosha.model.UserModel;
import com.example.mymiaosha.response.CommonReturn;
import com.example.mymiaosha.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author LL
 * @date 2021/9/11 15:02
 */
@RequestMapping("/order")
@Controller
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*",originPatterns = "*")
public class OrderController extends BaseController{

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @Autowired
    OrderService orderService;

    @RequestMapping(value = "/createorder",method = RequestMethod.POST,consumes = CONTENT_TYPE_FORMED)
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public CommonReturn createOrder(@RequestParam("itemId")Integer itemId,
                                    @RequestParam("amount")Integer amount) throws BusinessException {
        if (itemId==null||amount==null||amount<0){
            throw new BusinessException(EmBusinessError.PARAMETER_NOT_LEGITIMATE);
        }
        UserModel user = (UserModel) redisTemplate.opsForValue().get("user");
        if (user==null){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        orderService.createOrder(itemId, amount);
        return CommonReturn.create(null);
    }
}
