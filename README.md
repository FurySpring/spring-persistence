# Mybatis

## JDBC

#### 操作步骤：

1. 建立连接：

   加载数据库驱动

   通过驱动管理类获取数据库连接

2. 准备SQL：

   定义SQL语句

   获取预处理statement

   设置参数

   向数据库发起操作请求

3. 处理结果集：

   遍历结果集

   处理结果

#### 问题分析：

1. 数据库配置信息存在硬编码问题——配置文件

2. 频繁创建、释放数据库连接——连接池

3. SQL语句、参数、结果集均存在硬编码问题——配置文件

4. 处理结果集较为繁琐——反射、内省

### 自定义框架问题分析：

1. 重复代码
2. statementId存在硬编码

### 解决思路：

1. 使用代理模式创建接口的代理对象

2. 代理模式：JDK代理、cglib代理

## Mybatis缓存

### 一级缓存：

sqlsession级别，底层hashmap，默认开启

### 二级缓存：

跨sqlsession，mapper(namespace)级别，底层hashmap

问题：无法实现分布式缓存，不能跨服务器共享缓存

解决：对接分布式缓存Redis、memcached、ehcache等

##### Mybatis-redis--Rediscache

mybatis创建时创建

RedisConfig

putObject

Hset-->hash

#### 延迟加载：

嵌套查询，多次单表查询

动态代理，默认JavasistProxy、CglibProxy

### 插件

原理：动态代理生成代理对象执行请求

Executor

StatementHandler

ParameterHandler

ResultSetHandler

##### PageHelper

##### 通用mapper

## mybatis设计模式

##### *构建者模式：创建型模式*

构建多个简单对象，组装成复杂对象 sqlSessionFactoryBuilder XMLConfigBuilder buildConfiguration

##### *工厂模式：创建型模式*

根据需求生产不同对象，通常有相同父类 Factory openSession

##### *代理模式：对象结构模式*

MapperProxyFactory mapper.geMapper()

 

 