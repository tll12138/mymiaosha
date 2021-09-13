package com.example.mymiaosha.controller;

import com.example.mymiaosha.controller.viewproject.UserVO;
import com.example.mymiaosha.error.BusinessException;
import com.example.mymiaosha.error.EmBusinessError;
import com.example.mymiaosha.model.UserModel;
import com.example.mymiaosha.response.CommonReturn;
import com.example.mymiaosha.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author LL
 * @date 2021/8/30 15:34
 */
@Slf4j
@RequestMapping("/user")
@Controller
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*",originPatterns = "*")
public class UserController extends BaseController{

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/get")
    @ResponseBody
    public CommonReturn getUser(@RequestParam("id")Integer id) throws BusinessException {
        UserModel userModel = userService.getUserByid(id);
        if (userModel==null) {
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        UserVO userVO = convertViewObjectFromModel(userModel);
        return CommonReturn.create(userVO);
    }
    private UserVO convertViewObjectFromModel(UserModel userModel){
        if (userModel==null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);
        return userVO;
    }

    @RequestMapping(value = "/getotp",method = RequestMethod.POST,consumes = CONTENT_TYPE_FORMED)
    @ResponseBody
    public CommonReturn getOpt(@RequestParam("telphone")String telphone) throws BusinessException {
        if (telphone.length()!=11) {
            throw new BusinessException(EmBusinessError.PARAMETER_NOT_LEGITIMATE,"手机位数不正确");
        }else if (StringUtils.isEmpty(telphone)) {
            throw new BusinessException(EmBusinessError.PARAMETER_NOT_LEGITIMATE,"手机号不能为空");
        }
        String otp = String.valueOf(createOtp());
        redisTemplate.opsForValue().set(telphone, otp,3, TimeUnit.MINUTES);

        log.info("telphone是{}，opt是{}",telphone, otp);


        return CommonReturn.create(null);
    }

    /**
     * 创建100000-999999的随机数
     * @return
     */
    private int createOtp(){
        Random random = new Random();
        int randomInt = random.nextInt(899999);
        randomInt+=100000;
        return randomInt;
    }

    /**
     * 注册用户
     * @param telphone 注册用手机号
     * @param otp 手机验证码
     * @param userName 注册用户名
     * @param age 用户年龄
     * @param passWord 用户密码
     * @return
     */
    @RequestMapping(value = "/register",method = RequestMethod.POST,consumes = CONTENT_TYPE_FORMED)
    @ResponseBody
    public CommonReturn register(@RequestParam("telphone")String telphone,
                                   @RequestParam("otpCode")String otp,
                                   @RequestParam("name")String userName,
                                   @RequestParam("gender")Integer gender,
                                   @RequestParam("age")Integer age,
                                   @RequestParam("password")String passWord) throws BusinessException, UnsupportedEncodingException {

        String otpcode = (String) redisTemplate.opsForValue().get(telphone);
        if (otpcode==null) {
            throw new BusinessException(EmBusinessError.PARAMETER_NOT_LEGITIMATE,"请重新发送验证码");
        }

        if (!StringUtils.equals(otp, otpcode)) {
            throw new BusinessException(EmBusinessError.PARAMETER_NOT_LEGITIMATE,"验证码不正确");
        }

        UserModel userModel = new UserModel();
        userModel.setName(userName);
        userModel.setGender(gender);
        userModel.setPhone(telphone);
        userModel.setAge(age);
        userModel.setPassword(new String(Base64.encodeBase64(passWord.getBytes("UTF-8"))));

        userService.register(userModel);

        return CommonReturn.create(null);
    }

    @ResponseBody
    @RequestMapping(value = "/login",method = RequestMethod.POST,consumes = CONTENT_TYPE_FORMED)
    public CommonReturn login(@RequestParam("telphone")String phone,
                              @RequestParam("password")String password) throws BusinessException, UnsupportedEncodingException {

        if (StringUtils.isEmpty(phone)||StringUtils.isEmpty(password)) {
            throw new BusinessException(EmBusinessError.PARAMETER_NOT_LEGITIMATE,"手机号或或密码不能为空");
        }

        UserModel userModel = userService.getUserByPhone(phone);
        if (userModel==null) {
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        if (!StringUtils.equals(userModel.getPassword(), new String(Base64.encodeBase64(password.getBytes("UTF-8"))))) {
            throw new BusinessException(EmBusinessError.USER_LOGIN_ERROR);
        }

        redisTemplate.opsForValue().set("IS_LOGIN", true,15,TimeUnit.MINUTES);

        redisTemplate.opsForValue().set("user", userModel,15,TimeUnit.MINUTES);

        return CommonReturn.create(null);
    }
}
