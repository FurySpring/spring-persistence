package spring.config;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import spring.pojo.Configuration;
import spring.pojo.MappedStatement;

import java.io.InputStream;
import java.util.List;

/**
 * @author SpringWang
 * @date 2021/5/2
 */
public class XMLMapperBuilder {

    public Configuration configuration;
    private static final String[] MAPPER_NODES = {"//select", "//insert", "//update", "//delete"};

    public XMLMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public void parseConfig(InputStream inputStream) throws DocumentException {
        Document document = new SAXReader().read(inputStream);
        Element rootElement = document.getRootElement();
        String namespace = rootElement.attributeValue("namespace");
        for (String node : MAPPER_NODES) {
            parseNodes(namespace, rootElement.selectNodes(node));
        }
    }

    private void parseNodes(String namespace, List<Element> elements) {
        for (Element element : elements) {
            String id = element.attributeValue("id");
            String resultType = element.attributeValue("resultType");
            String parameterType = element.attributeValue("parameterType");
            String sqlText = element.getTextTrim();
            MappedStatement statement = MappedStatement.create(id, sqlText, parameterType, resultType);
            String key = namespace + "." + id;

            configuration.getMappedStatementMap().put(key, statement);
        }
    }
}
