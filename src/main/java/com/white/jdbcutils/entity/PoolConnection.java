package com.white.jdbcutils.entity;

import java.sql.Connection;

/**
 * @ClassName PoolConnection
 * @Author White.
 * @Date 2019/7/14 11:05
 * @Version 1.0
 * 连接的封装类
 */
public class PoolConnection {

    /**
     * 连接对象
     */
    private Connection connection;

    /**
     * 是否使用
     */
    private Boolean isUse;

    public PoolConnection(Connection connection) {
        this.connection = connection;
        this.isUse = false;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Boolean getUse() {
        return isUse;
    }

    public void setUse(Boolean use) {
        isUse = use;
    }
}
