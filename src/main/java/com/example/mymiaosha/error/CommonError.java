package com.example.mymiaosha.error;

/**
 * @author LL
 * @date 2021/8/30 20:43
 */
public interface CommonError {
    /**
     * 获取错误信息
     * @return
     */
    public String getErrMsg();

    /**
     * 获取错误信息代码
     * @return
     */
    public int getErrCode();

    /**
     * 设置错误信息
     * @param errMsg
     * @return
     */
    public CommonError setErrMsg(String errMsg);
}
