package com.spring.config;

import com.spring.utils.ParameterMapping;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author SpringWang
 * @date 2021/5/3
 */
@Data
@AllArgsConstructor
public class BoundSql {

    private String sqlText;

    private List<ParameterMapping> parameterMappings;
}
