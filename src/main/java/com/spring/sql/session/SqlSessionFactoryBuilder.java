package com.spring.sql.session;

import com.spring.config.XMLConfigBuilder;
import com.spring.pojo.Configuration;
import org.dom4j.DocumentException;

import java.beans.PropertyVetoException;
import java.io.InputStream;

/**
 * @author SpringWang
 * @date 2021/5/2
 */
public class SqlSessionFactoryBuilder {

    public SqlSessionFactoryBuilder() {
        Configuration configuration = new Configuration();
    }

    public SqlSessionFactory build(InputStream in) throws PropertyVetoException, DocumentException {
        // 1.使用dom4j解析dataSource封装到configuration中
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder();
        Configuration configuration = xmlConfigBuilder.parseConfig(in);

        // 2.创建sqlSessionFactory
        return new DefaultSqlSessionFactory(configuration);
    }
}
