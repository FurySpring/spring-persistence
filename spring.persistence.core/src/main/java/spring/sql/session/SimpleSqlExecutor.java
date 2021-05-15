package spring.sql.session;

import spring.config.BoundSql;
import spring.pojo.Configuration;
import spring.pojo.MappedStatement;
import spring.utils.GenericTokenParser;
import spring.utils.ParameterMapping;
import spring.utils.ParameterMappingTokenHandler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author SpringWang
 * @date 2021/5/3
 */
public class SimpleSqlExecutor implements SqlExecutor {

    @Override
    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception {
        PreparedStatement preparedStatement = buildPreparedStatement(configuration, mappedStatement, params);
        // 5.execute
        ResultSet resultSet = preparedStatement.executeQuery();
        // 6.处理结果集
        Class<?> resultTypeClass = getClassType(mappedStatement.getResultType());
        assert resultTypeClass != null;
        ArrayList<Object> objects = new ArrayList<>();
        while (resultSet.next()) {
            Object type = resultTypeClass.getDeclaredConstructor().newInstance();
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String columnName = metaData.getColumnName(i);
                Object columnValue = resultSet.getObject(columnName);
                // 使用反射或内省，根据数据库表字段名和实体的对应关系，封装结果
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultTypeClass);
                propertyDescriptor.getWriteMethod().invoke(type, columnValue);
            }
            objects.add(type);
        }
        return (List<E>) objects;
    }

    @Override
    public int update(Configuration configuration, MappedStatement statement, Object[] params) throws Exception {
        PreparedStatement preparedStatement = buildPreparedStatement(configuration, statement, params);
        return preparedStatement.executeUpdate();
    }

    private PreparedStatement buildPreparedStatement(Configuration configuration, MappedStatement mappedStatement, Object... params) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        // 1.注册驱动，获取连接
        Connection connection = configuration.getDataSource().getConnection();
        // 2.获取SQL
        // select * from User where id=#{id} and username=#{username}
        String sql = mappedStatement.getSql();
        //  2.1 转换sql
        BoundSql boundSql = getBoundSql(sql);
        // 3.获取预处理对象
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());
        Class<?> parameterTypeClass = getClassType(mappedStatement.getParameterType());
        // 4.设置参数
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        for (int i = 0; i < parameterMappings.size(); i++) {
            ParameterMapping parameterMapping = parameterMappings.get(i);
            String content = parameterMapping.getContent();
            // reflection
            assert parameterTypeClass != null;
            Field declaredField = parameterTypeClass.getDeclaredField(content);
            // 暴力访问
            declaredField.setAccessible(true);
            Object param = declaredField.get(params[0]);
            preparedStatement.setObject(i + 1, param);
        }
        return preparedStatement;
    }

    private Class<?> getClassType(String parameterType) throws ClassNotFoundException {
        if (Objects.nonNull(parameterType)) {
            return Class.forName(parameterType);
        }
        return null;
    }

    /**
     * 1.用?代替#{}; 2.保存#{}里的值
     */
    private BoundSql getBoundSql(String sql) {
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        String parsedSql = genericTokenParser.parse(sql);

        return new BoundSql(parsedSql, parameterMappingTokenHandler.getParameterMappings());
    }
}
