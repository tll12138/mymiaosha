package com.example.mymiaosha.error;

/**
 * @author LL
 * @date 2021/8/30 20:50
 */
public enum EmBusinessError implements CommonError{

    /**
     * 10000开头为通用异常
     */
    PARAMETER_NOT_LEGITIMATE(10001,"参数不正确"),

    /**
     * 20000开头为用户异常
     */
    USER_NOT_EXIST(20001,"该用户不存在"),
    USER_IS_EXIST(20002,"该手机号以注册用户"),
    USER_LOGIN_ERROR(20003,"输入的密码不正确"),
    USER_NOT_LOGIN(20004,"用户未登录"),

    /**
     * 30000开头为商品异常
     */
    ITEM_NOT_EXIST(30001,"商品不存在"),
    ITEM_NOT_ENOUGH(30002,"商品库存不足")
    ;

    private int errCode;
    private String errMsg;

    EmBusinessError(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg=errMsg;
        return this;
    }
}
