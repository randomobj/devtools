package com.gitee.randomobject.aspect;

import cn.hutool.aop.aspects.SimpleAspect;

import java.lang.reflect.Method;

public class ManSimpleAspect extends SimpleAspect {

    @Override
    public boolean before(Object target, Method method, Object[] args) {
        System.out.println("代理之前");
        return true;
    }

    @Override
    public boolean after(Object target, Method method, Object[] args) {
        System.out.println("代理之后");
        return false;
    }

    @Override
    public boolean after(Object target, Method method, Object[] args, Object returnVal) {
        System.out.println("代理之后的定时器");
        return super.after(target, method, args, returnVal);


    }

    @Override
    public boolean afterException(Object target, Method method, Object[] args, Throwable e) {
        System.out.println("代理之后的异常");
        return super.afterException(target, method, args, e);
    }
}
