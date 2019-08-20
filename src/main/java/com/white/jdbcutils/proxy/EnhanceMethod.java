package com.white.jdbcutils.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @InterfaceName EnhanceMethod
 * @Author White.
 * @Date 2019/6/25 9:16
 * @Version 1.0
 * 通知方法的函数表达式接口
 */
public interface EnhanceMethod {

    /**
     * 前置通知
     */
    void enhance();

    /**
     * 后置通知
     */
    void Postposition();
}
