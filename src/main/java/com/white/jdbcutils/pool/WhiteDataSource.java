package com.white.jdbcutils.pool;

import com.white.jdbcutils.entity.PoolConnection;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.LinkedList;
import java.util.logging.Logger;

/**
 * @ClassName WhiteDataSource
 * @Author White.
 * @Date 2019/7/14 10:21
 * @Version 1.0
 * 数据源类
 */
public class WhiteDataSource implements DataSource {

    /**
     * 连接驱动
     */
    private String driver;

    /**
     * 连接URl
     */
    private String url;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 初始连接数量
     */
    private Integer initialSize = 10;

    /**
     * 最大连接数量
     */
    private Integer maxActive = 20;

    /**
     * 连接池
     */
    private LinkedList<PoolConnection> connectionList;

    /**
     * 创建连接池
     */
    public WhiteDataSource init(){
        try {
            Class.forName(this.driver);
            for (int i = 0; i < this.initialSize; i++) {
                this.add();
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("没找到指定的driver路径 :" + this.driver);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 向连接池新增连接
     */
    private synchronized boolean add() throws SQLException {
        if (connectionList.size() >= maxActive){
            //无法创建连接
            return false;
        }
        //获取连接
        Connection conn = DriverManager.getConnection(url,username,password);
        //创建连接封装类
        PoolConnection poolConnection = new PoolConnection(conn);
        //创建代理对象
        Connection connProxy = this.getConnProxy(poolConnection);
        poolConnection.setConnection(connProxy);
        //装载进入连接池
        connectionList.add(poolConnection);
        return true;
    }

    /**
     * 创建动态代理对象
     * @param poolConn
     * @return
     */
    private Connection getConnProxy(PoolConnection poolConn){
        Connection connection = poolConn.getConnection();
        Connection conn = (Connection) Proxy.newProxyInstance(
                connection.getClass().getClassLoader(),
                connection.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if ("close".equals(method.getName())){
                            //归还连接
                            close(poolConn);
                            return null;
                        }

                        return method.invoke(connection,args);
                    }
                });
        return conn;
    }

    /**
     * 归还连接
     * @param poolConn
     */
    private synchronized void close(PoolConnection poolConn){
        poolConn.setUse(false);
        this.connectionList.remove(poolConn);
        this.connectionList.addLast(poolConn);
    }

    public WhiteDataSource(String driver, String url, String username, String password) {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
        this.connectionList = new LinkedList<>();
    }

    public WhiteDataSource() {
        this.connectionList = new LinkedList<>();
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(Integer initialSize) {
        this.initialSize = initialSize;
    }

    public Integer getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(Integer maxActive) {
        this.maxActive = maxActive;
    }


    /**
     * 获取可用连接
     * @return
     * @throws SQLException
     */
    @Override
    public synchronized Connection getConnection() throws SQLException {
        if (this.connectionList == null || this.connectionList.isEmpty()){
            throw new RuntimeException("连接池中没有连接");
        }
        //无限循环拿去可用连接
        while (true) {
            for (int i = 0; i < this.connectionList.size(); i++) {
                PoolConnection poolConnection = this.connectionList.get(i);
                //验证连接是否失效
                if (!poolConnection.getUse()){
                    if (!poolConnection.getConnection().isValid(0)){
                        poolConnection.setConnection(DriverManager.getConnection(url,username,password));
                    }
                    poolConnection.setUse(true);
                    return poolConnection.getConnection();
                }
            }
            //无可用连接，进行新增连接
            boolean flag = add();
            if (flag){
                return getConnection();
            }
        }
    }

    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    public void setLoginTimeout(int seconds) throws SQLException {

    }

    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
