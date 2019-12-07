package com.gitee.randomobject.utils;

/**
 * 字符串工具类
 */
public final class StringUtil {

    public static int getStrLength(String str){
        return str==null?0:str.length();
    }

    /**
     * 判断字符串是否为空
     * @param str 待检测字符串
     * @return true or false
     */
    public static boolean checkEmpty(String str){
        if (null==str||"".equalsIgnoreCase(str.replaceAll("\\s", ""))){
            return false;
        }
        return true;
    }

    /**
     * 判断字符传是否为空
     * @param str 待检测字符串
     * @param msg 自定义抛出信息
     */
    public static void checkEmpty(String str,String msg){
        if (null==str||"".equalsIgnoreCase(str)){
            throw new IllegalArgumentException(msg);
        }
    }

}
