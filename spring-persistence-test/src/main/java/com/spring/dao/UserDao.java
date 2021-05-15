package com.spring.dao;

import com.spring.pojo.User;

import java.util.List;

/**
 * @author SpringWang
 * @date 2021/5/4
 */
public interface UserDao {

    User selectOne(User user);

    List<User> selectList();

    int insert(User user);

    int update(User user);

    int delete(User user);
}
