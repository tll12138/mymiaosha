package com.example.mymiaosha.service;

import com.example.mymiaosha.error.BusinessException;
import com.example.mymiaosha.model.UserModel;

/**
 * @author LL
 * @date 2021/8/30 15:33
 */
public interface UserService {
    /**
     * 使用id搜索用户
     * @param id 查询的用户id
     */
    UserModel getUserByid(Integer id);

    /**
     * 用户注册
     * @param userModel
     */
    void register(UserModel userModel) throws BusinessException;

    /**
     * 根据手机号查找是否存在该用户
     * @param phone
     * @return
     */
    UserModel getUserByPhone(String phone) throws BusinessException;
}
