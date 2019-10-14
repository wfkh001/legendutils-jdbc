package com.white.jdbcutils.core;

import com.google.gson.*;
import com.white.jdbcutils.annotation.ToJson;
import com.white.jdbcutils.utils.StringUtils;

import javax.sql.DataSource;
import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @ClassName JdbcTemplate
 * @Author White.
 * @Date 2019/7/14 15:26
 * @Version 1.0
 * 对执行语句的封装类
 */
public class JdbcTemplate implements JdbcOperations{

    /**
     * 数据源
     */
    private DataSource dataSource;

    private Gson gson;

    /**
     * 获取连接
     * @return
     */
    private Connection getConnection(){
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 设置参数
     * @param statement
     * @param param
     * @return
     */
    private PreparedStatement setValue(PreparedStatement statement , Object[] param) throws SQLException {
        for (int i = 0; i < param.length; i++) {
            Object o = param[i];
            if (o instanceof Integer){
                statement.setInt(i+1, (Integer) o);
            }else if (o instanceof Long){
                statement.setLong(i+1, (Long) o);
            }else if (o instanceof Byte){
                statement.setByte(i+1, (Byte) o);
            }else if (o instanceof Short){
                statement.setShort(i+1, (Short) o);
            }else if (o instanceof Float){
                statement.setFloat(i+1, (Float) o);
            }else if (o instanceof Double){
                statement.setDouble(i+1, (Double) o);
            }else if (o instanceof String){
                statement.setString(i+1, (String) o);
            }else if (o instanceof Boolean){
                statement.setBoolean(i+1, (Boolean) o);
            }else if (o instanceof Date){
                statement.setDate(i+1, (java.sql.Date) o);
            }
        }
        return statement;
    }

    /**
     * 将Set结果集的数据封装到实体类中
     * @param resultSet
     * @param cls
     * @param <T>
     * @return
     * @throws SQLException
     */
    private <T> List<T> encapsulationData(ResultSet resultSet , Class<T> cls) throws SQLException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        List<T> list = new ArrayList<>();
        while (resultSet.next()){
            //实例化指定Class的对象
            T t = cls.newInstance();
            //获取该类的所有属性
            Field[] fields = cls.getDeclaredFields();
            //遍历实体类中的属性
            for (Field field : fields) {
                //将实体类中的属性名转换成表的字段名
                String lineName = StringUtils.humpToLine(field.getName());
                //实体类的某属性对象值
                Object fieldTypeObj = null;
                //判断是否为复杂类型
                if (Objects.nonNull(field.getAnnotation(ToJson.class))){
                    //从resultSet结果集中获取指定字段名的值
                    String json = resultSet.getObject(lineName, String.class);
                    if (json != null && json.length() != 0){
                        //获取属性值
                        if (List.class.equals(field.getType())){
                            List resultList = new ArrayList();
                            JsonArray arry = new JsonParser().parse(json).getAsJsonArray();
                            Type genericType = field.getGenericType();
                            Class<?> actualTypeArgument = null;
                            if (genericType != null) {
                                if (genericType instanceof ParameterizedType) {
                                    ParameterizedType pt = (ParameterizedType) genericType;
                                    // 得到泛型里的class类型对象
                                    actualTypeArgument = (Class<?>)pt.getActualTypeArguments()[0];
                                }
                            }
                            if (actualTypeArgument != null) {
                                for (JsonElement jsonElement : arry) {
                                    resultList.add(gson.fromJson(jsonElement, actualTypeArgument));
                                }
                                fieldTypeObj = resultList;
                            }
                        }else if (Set.class.equals(field.getType())){
                            Set resultList = new HashSet();
                            JsonArray arry = new JsonParser().parse(json).getAsJsonArray();
                            Type genericType = field.getGenericType();
                            Class<?> actualTypeArgument = null;
                            if (genericType != null) {
                                if (genericType instanceof ParameterizedType) {
                                    ParameterizedType pt = (ParameterizedType) genericType;
                                    // 得到泛型里的class类型对象
                                    actualTypeArgument = (Class<?>)pt.getActualTypeArguments()[0];
                                }
                            }
                            if (actualTypeArgument != null) {
                                for (JsonElement jsonElement : arry) {
                                    resultList.add(gson.fromJson(jsonElement, actualTypeArgument));
                                }
                                fieldTypeObj = resultList;
                            }
                        }else if (Map.class.equals(field.getType())) {
                            Map resultMap = new HashMap();
                            JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
                            Type genericType = field.getGenericType();
                            //泛型类型
                            Class<?> actualTypeArgumentOne = null;
                            Class<?> actualTypeArgumentTwe = null;
                            if (genericType != null) {
                                if (genericType instanceof ParameterizedType) {
                                    ParameterizedType pt = (ParameterizedType) genericType;
                                    // 得到泛型里的class类型对象
                                    actualTypeArgumentOne = (Class<?>)pt.getActualTypeArguments()[0];
                                    actualTypeArgumentTwe = (Class<?>)pt.getActualTypeArguments()[1];
                                }
                            }
                            if (actualTypeArgumentOne != null && actualTypeArgumentTwe != null) {
                                for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                                    String key = entry.getKey();
                                    JsonElement value = entry.getValue();
                                    Object objKey = null;
                                    //转换key
                                    if (Integer.class.equals(actualTypeArgumentOne)) {
                                        objKey = Integer.parseInt(key);
                                    } else if (Long.class.equals(actualTypeArgumentOne)) {
                                        objKey = Long.parseLong(key);
                                    } else if (Byte.class.equals(actualTypeArgumentOne)) {
                                        objKey = Byte.parseByte(key);
                                    } else if (Short.class.equals(actualTypeArgumentOne)) {
                                        objKey = Short.parseShort(key);
                                    } else if (Float.class.equals(actualTypeArgumentOne)) {
                                        objKey = Float.parseFloat(key);
                                    } else if (Double.class.equals(actualTypeArgumentOne)) {
                                        objKey = Double.parseDouble(key);
                                    } else if (String.class.equals(actualTypeArgumentOne)) {
                                        objKey = key;
                                    } else if (Boolean.class.equals(actualTypeArgumentOne)) {
                                        objKey = Boolean.parseBoolean(key);
                                    }
                                    resultMap.put(objKey,gson.fromJson(value, actualTypeArgumentTwe));
                                }
                                fieldTypeObj = resultMap;
                            }
                        }else {
                            fieldTypeObj = gson.fromJson(json, field.getType());
                        }
                    }
                }else {
                    fieldTypeObj = resultSet.getObject(lineName, field.getType());
                }
                //获得实体类中属性名对于的set方法
                Method method = cls.getMethod(StringUtils.attrToMethodName(field.getName()), field.getType());
                //执行set方法属性装载成功
                method.invoke(t,fieldTypeObj);
            }
            list.add(t);
        }
        return list;
    }

    /**
     * 关闭资源
     * @param resultSet
     * @param statement
     */
    private void close(ResultSet resultSet,PreparedStatement statement , Connection conn){
        if (resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (statement != null ){
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 查询所有
     * @param sql 执行的select语句
     * @param cls 泛型类型
     * @param param 替换字符的参数列表
     * @param <T>
     * @return
     */
    @Override
    public <T> List<T> findAll(String sql, Class<T> cls, Object... param) {
        Connection connection = getConnection();
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        List<T> list = new ArrayList<>();
        if (connection == null){
            return new ArrayList<>();
        }
        try {
            statement = connection.prepareStatement(sql);
            //设置参数值
            setValue(statement,param);
            resultSet= statement.executeQuery();
            try {
                list = encapsulationData(resultSet, cls);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            close(resultSet,statement,connection);
        }
        return list;
    }

    /**
     * 查询一个
     * @param sql 执行的select语句
     * @param cls 返回值类型
     * @param param 替换字符的参数列表
     * @param <T>
     * @return
     */
    @Override
    public <T> T findOne(String sql, Class<T> cls, Object... param) {
        List<T> list = findAll(sql, cls, param);
        //断言结果只有一个
        assert list!=null&&list.size()==1;
        return list.get(0);
    }

    /**
     * 查询记录数
     * @param sql 执行的select语句
     * @param param 替换字符的参数列表
     * @return
     */
    @Override
    public long findCount(String sql, Object... param) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        long count = 0;
        Connection connection = getConnection();
        if (connection == null){
            return 0;
        }
        try {
            statement = connection.prepareStatement(sql);
            //设置参数值
            setValue(statement,param);
            resultSet = statement.executeQuery();
            while (resultSet.next()){
                count++;
            }
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            close(resultSet,statement,connection);
        }
        return 0;
    }

    /**
     * 修改
     * @param sql 修改的语句
     * @param param 替换字符的参数列表
     * @return
     */
    @Override
    public int update(String sql, Object... param) {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        int count = 0;
        if (connection == null){
            return 0;
        }
        try {
            statement = connection.prepareStatement(sql);
            setValue(statement,param);
            count = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            close(null,statement,connection);
        }
        return count;
    }

    /**
     * 删除
     * @param sql sql语句
     * @param param 替换字符的参数列表
     * @return
     */
    @Override
    public int delete(String sql, Object... param) {
        return update(sql,param);
    }

    /**
     * 新增
     * @param sql sql语句
     * @param param 替换字符的参数列表
     * @return
     */
    @Override
    public int insert(String sql, Object... param) {
        return update(sql,param);
    }

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
        this.gson = new Gson();
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

}