package com.white.jdbcutils.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @ClassName ProxyUtils
 * @Author White.
 * @Date 2019/6/25 9:19
 * @Version 1.0
 * 动态代理的工具类
 */
public class ProxyUtils {

    private ProxyUtils(){}

    /**
     * 传入被代理对象返回代理对象
     * @param obj 被代理对象
     * @param cls 返回类型,用被代理的什么接口接
     * @param <T> 返回泛型
     * @param enhanceMethod 通知方法
     * @return
     */
    public static <T> T proxy(Object obj, Class<T> cls, EnhanceMethod enhanceMethod){
        //动态代理
        return  (T) Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                enhanceMethod.enhance();
                Object returnObj = method.invoke(obj,args);
                enhanceMethod.Postposition();
                return returnObj;
            }
        });
    }
}
