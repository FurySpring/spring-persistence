package com.spring.pojo;

import lombok.Data;

/**
 * @author SpringWang
 * @date 2021/5/2
 */
@Data
public class MappedStatement {

    private String id;

    private String sql;

    private String parameterType;

    private String resultType;

    public static MappedStatement create(String id, String sql, String parameterType, String resultType) {
        return new MappedStatement(id, sql, parameterType, resultType);
    }

    private MappedStatement(String id, String sql, String parameterType, String resultType) {
        this.id = id;
        this.sql = sql;
        this.parameterType = parameterType;
        this.resultType = resultType;
    }
}
