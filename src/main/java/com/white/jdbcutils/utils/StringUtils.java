package com.white.jdbcutils.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName StringUtils
 * @Author White.
 * @Date 2019/7/14 17:40
 * @Version 1.0
 */
public class StringUtils {

    /**
     * 下划线转驼峰正则表达式
     */
    private static Pattern linePattern = Pattern.compile("_(\\w)");

    /**
     * 驼峰转下划线正则表达式
     */
    private static Pattern humpPattern = Pattern.compile("[A-Z]");


    /**
     * 下划线转驼峰
     * @param str
     * @return
     */
    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }


    /**
     * 驼峰转下划线
     * @param str
     * @return
     */
    public static String humpToLine(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 驼峰属性名转set方法名
     * @param str
     * @return
     */
    public static String attrToMethodName(String str){
        str = str.substring(0,1).toUpperCase().concat(str.substring(1).toLowerCase());
        return "set"+str;
    }

}

