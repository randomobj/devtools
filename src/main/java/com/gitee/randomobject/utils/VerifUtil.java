package com.gitee.randomobject.utils;

import cn.hutool.core.codec.Base64;
import com.gitee.randomobject.captcha.GifCaptcha;
import com.gitee.randomobject.captcha.SpecCaptcha;

import java.io.ByteArrayOutputStream;

/**
 * 验证码工具类
 */
public final class VerifUtil {


    private VerifUtil() {
    }

    /**
     * 得到gif生成器 默认宽度140，高度50
     * @return GifCaptcha
     */
    public static GifCaptcha createGif() {
        return new GifCaptcha();
    }

    /**
     * 得到png格式的生成器 默认宽度140，高度50
     * @return SpecCaptcha
     */
    public static SpecCaptcha createPng(){
        return new SpecCaptcha();
    }

    /**
     * 得到base64,默认宽度140，高度50
     * @return string
     */
    public static String gifToBase64(){
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        new GifCaptcha().out(os);
        return Base64.encode(os.toByteArray());
    }

    /**
     * 得到base64 默认宽度140，高度50
     * @return string
     */
    public static String pngToBase64(){
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        new SpecCaptcha().out(os);
        return Base64.encode(os.toByteArray());
    }

    /**
     * 得到指定高度、宽度、和验证码个数的Gif生成器
     * @param width 宽度
     * @param height 高度
     * @param len 验证码个数
     * @return GifCaptcha
     */
    public static GifCaptcha createGif(int width, int height, int len){
        return new GifCaptcha(width,height,len);
    }

    /**
     * 得到指定高度、宽度的gif生成器
     * @param width 宽度
     * @param height 高度
     * @return GifCaptcha
     */
    public static GifCaptcha createGif(int width, int height){
        return new GifCaptcha(width, height);
    }

    /**
     * 得到png格式生成器
     * @param width 宽度
     * @param height 高度
     * @return SpecCaptcha
     */
    public static SpecCaptcha createPng(int width, int height){
        return new SpecCaptcha(width, height);
    }

    /**
     * 得到指定宽度、高度、验证码个数的png格式生成器
     * @param width 宽度
     * @param height 高度
     * @param len 验证码个数
     * @return SpecCaptcha
     */
    public static SpecCaptcha createPng(int width, int height,int len){
        return new SpecCaptcha(width, height,len);
    }



}
