package com.example.mymiaosha.service.impl;

import com.example.mymiaosha.bean.UserInfo;
import com.example.mymiaosha.bean.UserPassword;
import com.example.mymiaosha.dao.UserInfoMapper;
import com.example.mymiaosha.dao.UserPasswordMapper;
import com.example.mymiaosha.error.BusinessException;
import com.example.mymiaosha.error.EmBusinessError;
import com.example.mymiaosha.model.UserModel;
import com.example.mymiaosha.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author LL
 * @date 2021/9/1 16:30
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    UserPasswordMapper userPasswordMapper;

    @Override
    public UserModel getUserByid(Integer id) {
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(id);
        if (userInfo==null) {
            return null;
        }
        UserModel userModel = convertModelFromBean(userInfo);
        return userModel;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(UserModel userModel) throws BusinessException {
        if (userModel==null) {
            throw new BusinessException(EmBusinessError.PARAMETER_NOT_LEGITIMATE);
        }
        if (StringUtils.isEmpty(userModel.getPhone())||
                StringUtils.isEmpty(userModel.getName())||
                StringUtils.isEmpty(userModel.getPassword())||
                userModel.getAge()==null||
                userModel.getGender()==null) {
            throw new BusinessException(EmBusinessError.PARAMETER_NOT_LEGITIMATE);
        }
        UserInfo userInfo = convertUserFromModel(userModel);
        try {
            userInfoMapper.insertSelective(userInfo);
        } catch (DuplicateKeyException e) {
            throw new BusinessException(EmBusinessError.USER_IS_EXIST);
        }
        userModel.setId(userInfo.getId());
        UserPassword userPassword = convertUserPassWordFromModel(userModel);

        userPasswordMapper.insertSelective(userPassword);
        return;
    }

    @Override
    public UserModel getUserByPhone(String phone) throws BusinessException {
        if (StringUtils.isEmpty(phone)) {
            return null;
        }
        UserInfo userInfo = userInfoMapper.selectByPhone(phone);
        if (userInfo==null) {
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        UserModel userModel = convertModelFromBean(userInfo);
        userModel.setPassword(userPasswordMapper.selectByUserId(userModel.getId()).getPassword());
        return userModel;
    }

    private UserModel convertModelFromBean(UserInfo userInfo){
        if (userInfo==null) {
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userInfo, userModel);
        userModel.setPassword(userPasswordMapper.selectByUserId(userModel.getId()).getPassword());
        return userModel;
    }

    private UserInfo convertUserFromModel(UserModel userModel){
        if (userModel==null) {
            return null;
        }
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userModel, userInfo);
        return userInfo;
    }
    private UserPassword convertUserPassWordFromModel(UserModel userModel){
        if (userModel==null) {
            return null;
        }
        UserPassword userPassword = new UserPassword();
        BeanUtils.copyProperties(userModel, userPassword);
        userPassword.setUserid(userModel.getId());
        return userPassword;
    }
}
