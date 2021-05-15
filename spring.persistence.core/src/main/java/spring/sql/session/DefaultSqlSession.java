package spring.sql.session;

import spring.pojo.Configuration;
import spring.pojo.MappedStatement;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
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
        List<T> list = selectList(statementId, params);
        if (list.size() == 1) {
            return list.get(0);
        } else if (list.size() > 1) {
            throw new RuntimeException("查询结果为多个");
        } else {
            return null;
        }
    }

    @Override
    public <E> List<E> selectList(String statementId, Object... params) throws Exception {
        SimpleSqlExecutor executor = new SimpleSqlExecutor();
        MappedStatement statement = configuration.getMappedStatementMap().get(statementId);
        return executor.query(configuration, statement, params);
    }

    @Override
    public int insert(String statementId, Object... params) throws Exception {
        SimpleSqlExecutor executor = new SimpleSqlExecutor();
        MappedStatement statement = configuration.getMappedStatementMap().get(statementId);
        return executor.update(configuration, statement, params);
    }

    @Override
    public int update(String statementId, Object... params) throws Exception {
        SimpleSqlExecutor executor = new SimpleSqlExecutor();
        MappedStatement statement = configuration.getMappedStatementMap().get(statementId);
        return executor.update(configuration, statement, params);
    }

    @Override
    public int delete(String statementId, Object... params) throws Exception {
        SimpleSqlExecutor executor = new SimpleSqlExecutor();
        MappedStatement statement = configuration.getMappedStatementMap().get(statementId);
        return executor.update(configuration, statement, params);
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
                switch (methodName) {
                    case "selectOne":
                        return selectOne(statementId, args);
                    case "selectList":
                        return selectList(statementId, args);
                    case "insert":
                        return insert(statementId, args);
                    case "update":
                        return update(statementId, args);
                    case "delete":
                        return delete(statementId, args);
                }
                throw new IllegalAccessException("No such method: " + methodName);
            }
        });

        return (T) proxyInstance;
    }
}
