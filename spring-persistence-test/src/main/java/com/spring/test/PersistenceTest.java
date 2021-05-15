package com.spring.test;

import com.spring.dao.UserDao;
import com.spring.pojo.User;
import org.dom4j.DocumentException;
import org.junit.Test;
import spring.io.Resource;
import spring.sql.session.SqlSession;
import spring.sql.session.SqlSessionFactory;
import spring.sql.session.SqlSessionFactoryBuilder;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;

/**
 * @author SpringWang
 * @date 2021/5/2
 */
public class PersistenceTest {

    @Test
    public void testSelectOne() throws Exception {
        User userCondition = User.create(1);
        User user = openSession().selectOne("com.spring.dao.UserDao.selectOne", userCondition);
        System.out.println(user);
    }

    @Test
    public void testSelectList() throws Exception {
        List<User> users = openSession().selectList("com.spring.dao.UserDao.selectList");
        for (User user1 : users) {
            System.out.println(user1);
        }
    }

    @Test
    public void testProxySelectOne() throws Exception {
        UserDao userDao = openSession().getMapper(UserDao.class);
        User user = userDao.selectOne(User.create(1));
        System.out.println(user);
    }

    @Test
    public void testProxySelectList() throws Exception {
        UserDao userDao = openSession().getMapper(UserDao.class);
        List<User> users = userDao.selectList();
        for (User user1 : users) {
            System.out.println(user1);
        }
    }

    @Test
    public void testProxyInsert() throws Exception {
        UserDao userDao = openSession().getMapper(UserDao.class);
        User user = User.create(3, "liquid", "666", "2020-02-22");
        int affectedRows = userDao.insert(user);
        System.out.println("affected rows: " + affectedRows);
        System.out.println("new user: " + userDao.selectOne(user));
    }

    @Test
    public void testProxyUpdate() throws Exception {
        UserDao userDao = openSession().getMapper(UserDao.class);
        User user = User.create(3, "all new liquid");
        int affectedRows = userDao.update(user);
        System.out.println("affected rows: " + affectedRows);
        System.out.println("updated user: " + userDao.selectOne(User.create(3)));
    }

    @Test
    public void testProxyDelete() throws Exception {
        UserDao userDao = openSession().getMapper(UserDao.class);
        User user = User.create(3);
        int affectedRows = userDao.delete(user);
        System.out.println("affected rows: " + affectedRows);
        System.out.println("after deleted user3: " + userDao.selectOne(user));
    }

    private SqlSession openSession() throws PropertyVetoException, DocumentException {
        InputStream resourceAsStream = Resource.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        return sqlSessionFactory.openSession();
    }
}
