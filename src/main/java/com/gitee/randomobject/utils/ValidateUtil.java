package com.gitee.randomobject.utils;

/**
 * 对象工具类
 */
public class ValidateUtil {


    public static void checkNull(Object o, String msg) {
        if (null == o) throw new IllegalArgumentException(msg);
    }

    public static boolean checkNull(Object o) {
        return null == o ? false : true;
    }

}
