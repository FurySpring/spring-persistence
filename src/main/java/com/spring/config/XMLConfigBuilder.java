package com.spring.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.spring.io.Resource;
import com.spring.pojo.Configuration;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * @author SpringWang
 * @date 2021/5/2
 */
public class XMLConfigBuilder {

    public Configuration configuration;

    public XMLConfigBuilder() {
        this.configuration = new Configuration();
    }

    /**
     * 使用dom4j解析配置文件，并封装configuration
     */
    public Configuration parseConfig(InputStream in) throws DocumentException, PropertyVetoException {
        Document document = new SAXReader().read(in);
        Element rootElement = document.getRootElement();
        List<Element> propertyElements = rootElement.selectNodes("//property");
        Properties properties = new Properties();
        for (Element element : propertyElements) {
            String name = element.attributeValue("name");
            String value = element.attributeValue("value");
            properties.setProperty(name, value);
        }

        // connection pool
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        comboPooledDataSource.setDriverClass(properties.getProperty("driverClass"));
        comboPooledDataSource.setJdbcUrl(properties.getProperty("jdbcUrl"));
        comboPooledDataSource.setUser(properties.getProperty("user"));
        comboPooledDataSource.setPassword(properties.getProperty("password"));
        configuration.setDataSource(comboPooledDataSource);

        // mapper.xml解析：path->inputStream->parse
        List<Element> mapperElements = rootElement.selectNodes("//mapper");
        for (Element element : mapperElements) {
            String mapperPath = element.attributeValue("resource");
            InputStream resourceAsStream = Resource.getResourceAsStream(mapperPath);
            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(configuration);
            xmlMapperBuilder.parseConfig(resourceAsStream);
        }

        return configuration;
    }
}
