package com.spring.sql.session;

import com.spring.pojo.Configuration;
import com.spring.pojo.MappedStatement;

import java.util.List;

/**
 * @author SpringWang
 * @date 2021/5/3
 */
public interface SqlExecutor {

    <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception;

    void update(Configuration configuration, MappedStatement statement, Object[] params) throws Exception;
}
