package com.spring.sql.session;

import java.util.List;

/**
 * @author SpringWang
 * @date 2021/5/3
 */
public interface SqlSession {

    // select list
    <E> List<E> selectList(String statementId, Object... params) throws Exception;

    // select one
    <T> T selectOne(String statementId, Object... params) throws Exception;

    void insert(String statementId, Object... params) throws Exception;

    void update(String statementId, Object... params) throws Exception;

    void delete(String statementId, Object... params) throws Exception;

    <T> T getMapper(Class<?> mapperClass);
}
