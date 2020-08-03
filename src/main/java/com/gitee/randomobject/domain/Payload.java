package com.gitee.randomobject.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Payload<T> {
    /**UUID*/
    private String id;
    /**返回信息*/
    private T returnInfo;
    /**有效时间*/
    private Date expire;

}
