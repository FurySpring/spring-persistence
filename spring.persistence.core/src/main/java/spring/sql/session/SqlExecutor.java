package spring.sql.session;

import spring.pojo.Configuration;
import spring.pojo.MappedStatement;

import java.util.List;

/**
 * @author SpringWang
 * @date 2021/5/3
 */
public interface SqlExecutor {

    <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception;

    int update(Configuration configuration, MappedStatement statement, Object[] params) throws Exception;
}
