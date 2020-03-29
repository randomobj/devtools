package com.gitee.randomobject.aspect;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

public class Man implements Person {

    private Log log = LogFactory.get(Man.class);

    @Override
    public void eat(String str) {

        log.info("[男人吃东西]:msg:{}", str);

    }
}
