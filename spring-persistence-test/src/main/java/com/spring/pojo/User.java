package com.spring.pojo;

import lombok.Data;

/**
 * @author SpringWang
 * @date 2021/5/3
 */
@Data
public class User {

    private Integer id;

    private String username;

    private String password;

    private String birthday;


    public static User create(Integer id) {
        return create(id, null, null, null);
    }

    public static User create(Integer id, String username) {
        return create(id, username, null, null);
    }

    public static User create(Integer id, String username, String password, String birthday) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setBirthday(birthday);
        return user;
    }
}
