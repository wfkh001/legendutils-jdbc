package com.white.jdbcutils.core;

import java.util.List;

/**
 * @InterfaceName JdbcOperations
 * @Author White.
 * @Date 2019/7/14 15:28
 * @Version 1.0
 * 对SQL语句操作的规范接口
 */
public interface JdbcOperations {

    /**
     * 查询所有
     * @param sql 执行的select语句
     * @param cls 泛型类型
     * @param param 替换字符的参数列表
     * @param <T>
     * @return
     */
    <T> List<T> findAll(String sql , Class<T> cls , Object... param);

    /**
     * 查询一个
     * @param sql 执行的select语句
     * @param cls 返回值类型
     * @param param 替换字符的参数列表
     * @param <T>
     * @return
     */
    <T> T findOne(String sql , Class<T> cls , Object... param);

    /**
     * 查询记录数
     * @param sql 执行的select语句
     * @param param 替换字符的参数列表
     * @return 查询到的记录数
     */
    long findCount(String sql , Object... param);

    /**
     * 修改
     * @param sql 修改的语句
     * @param param 替换字符的参数列表
     * @return 影响行数
     */
    int update(String sql , Object... param);

    /**
     * 删除
     * @param sql sql语句
     * @param param 替换字符的参数列表
     * @return 影响行数
     */
    int delete(String sql , Object... param);

    /**
     * 新增
     * @param sql sql语句
     * @param param 替换字符的参数列表
     * @return 影响行数
     */
    int insert(String sql , Object... param);
}
