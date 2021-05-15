package com.spring.sql.session;

import com.spring.pojo.Configuration;
import com.spring.pojo.MappedStatement;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * @author SpringWang
 * @date 2021/5/3
 */
public class DefaultSqlSession implements SqlSession {

    private final Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <T> T selectOne(String statementId, Object... params) throws Exception {
        List<Object> list = selectList(statementId, params);
        if (list.size() == 1) {
            return (T) list.get(0);
        } else {
            throw new RuntimeException("查询结果为空或多个");
        }
    }

    @Override
    public <E> List<E> selectList(String statementId, Object... params) throws Exception {
        SimpleSqlExecutor executor = new SimpleSqlExecutor();
        MappedStatement statement = configuration.getMappedStatementMap().get(statementId);
        return executor.query(configuration, statement, params);
    }

    @Override
    public void insert(String statementId, Object... params) throws Exception {
        SimpleSqlExecutor executor = new SimpleSqlExecutor();
        MappedStatement statement = configuration.getMappedStatementMap().get(statementId);
        executor.update(configuration, statement, params);
    }

    @Override
    public void update(String statementId, Object... params) throws Exception {
        SimpleSqlExecutor executor = new SimpleSqlExecutor();
        MappedStatement statement = configuration.getMappedStatementMap().get(statementId);
        executor.update(configuration, statement, params);
    }

    @Override
    public void delete(String statementId, Object... params) throws Exception {
        SimpleSqlExecutor executor = new SimpleSqlExecutor();
        MappedStatement statement = configuration.getMappedStatementMap().get(statementId);
        executor.update(configuration, statement, params);
    }

    @Override
    public <T> T getMapper(Class<?> mapperClass) {
        Object proxyInstance = Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[]{mapperClass}, new InvocationHandler() {
            /**
             * proxy: 当前代理对象的引用
             * method: 当前代理方法的引用
             * args: 参数(user)
             */
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 底层执行jdbc代码，根据情况调用selectOne或selectList
                // statementId = 接口全限定名.方法名，和mapper的namespace和select id对应
                String methodName = method.getName();
                String methodClassName = method.getDeclaringClass().getName();
                String statementId = methodClassName + "." + methodName;
                if (method.getGenericReturnType() instanceof ParameterizedType) {
                    return selectList(statementId, args);
                }
                return selectOne(statementId, args);
            }
        });

        return (T) proxyInstance;
    }
}
