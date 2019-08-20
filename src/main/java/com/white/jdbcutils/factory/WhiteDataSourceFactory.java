package com.white.jdbcutils.factory;

import com.white.jdbcutils.pool.WhiteDataSource;

/**
 * @ClassName WhiteDataSourceFactory
 * @Author White.
 * @Date 2019/7/14 10:21
 * @Version 1.0
 * 数据源工厂类
 */
public class WhiteDataSourceFactory {

    /**
     * 私有构造
     */
    private WhiteDataSourceFactory(){}

    /**
     * 数据源
     */
    private static WhiteDataSource whiteDataSource;

    /**
     * 创建数据源
     * 这个数据源是单例的
     * @param driver 驱动信息
     * @param url URL
     * @param username 用户名
     * @param passowrd 密码
     * @return 返回的是一个单例对象
     */
    public static WhiteDataSource createWhiteDataSource(String driver,String url,String username,String passowrd){
        if (whiteDataSource == null){
            WhiteDataSource whiteDataSource = new WhiteDataSource();
            whiteDataSource.setDriver(driver);
            whiteDataSource.setUrl(url);
            whiteDataSource.setUsername(username);
            whiteDataSource.setPassword(passowrd);
            WhiteDataSourceFactory.whiteDataSource = whiteDataSource;
            return whiteDataSource;
        }
        return whiteDataSource;
    }
}
