package com.example.mymiaosha.model;

import lombok.Data;

/**
 * @author LL
 * @date 2021/9/1 16:33
 */
@Data
public class UserModel {
    private Integer id;

    private String name;

    private Integer gender;

    private String phone;

    private Integer age;

    private String password;
}
