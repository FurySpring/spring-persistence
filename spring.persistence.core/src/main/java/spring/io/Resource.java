package spring.io;

import java.io.InputStream;

/**
 * @author SpringWang
 * @date 2021/5/2
 */
public class Resource {

    public static InputStream getResourceAsStream(String path) {
        return Resource.class.getClassLoader().getResourceAsStream(path);
    }
}
