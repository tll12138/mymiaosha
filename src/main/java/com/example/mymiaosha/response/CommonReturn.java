package com.example.mymiaosha.response;

import lombok.Data;

/**
 * @author LL
 * @date 2021/8/30 15:36
 */
@Data
public class CommonReturn {
    /**
     * status:success或fail
     * data:返回前端的数据
     */
    private String status;
    private Object data;

    public static CommonReturn create(Object data){
        return CommonReturn.create(data, "success");
    }

    public static CommonReturn create(Object data,String status){
        CommonReturn commonReturn = new CommonReturn();
        commonReturn.data=data;
        commonReturn.status=status;

        return commonReturn;
    }
}
