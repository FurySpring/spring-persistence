package spring.sql.session;

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

    int insert(String statementId, Object... params) throws Exception;

    int update(String statementId, Object... params) throws Exception;

    int delete(String statementId, Object... params) throws Exception;

    <T> T getMapper(Class<?> mapperClass);
}
