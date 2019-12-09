package com.gitee.randomobject.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import static java.util.Base64.*;


/**
 * 字符串加密工具类
 */
public class MessageDigestUtil {

    /**
     * 加密生成器
     *
     * @param algorithm 加密类型
     * @param str       待加密字符串
     * @return 已加密字符串
     */
    public static String encrpy(String algorithm, String str) {
        try {
            StringBuilder sb = new StringBuilder();
            MessageDigest md = MessageDigest.getInstance(algorithm.toUpperCase());
            md.update(str.getBytes("UTF-8"));
            byte[] digest = md.digest();
            for (int i = 0; i < digest.length; i++) {
                sb.append(Integer.toHexString((0x000000ff & digest[i]) | 0xffffff00).substring(6));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * base64 加密
     * @param str 待加密字符串
     * @return 已加密字符串
     */
    public static String encoderStr(String str) {
        return getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * base64解密字符串
     * @param str 待解密字符串
     * @return 已解密字符串
     */
    public static String decoderStr(String str) {
        byte[] decode = getDecoder().decode(str);
        return new String(decode, StandardCharsets.UTF_8);
    }

}
