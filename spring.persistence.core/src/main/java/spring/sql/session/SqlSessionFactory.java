package spring.sql.session;

/**
 * @author SpringWang
 * @date 2021/5/2
 */
public interface SqlSessionFactory {

    SqlSession openSession();
}
