package com.gitee.randomobject.utils;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.gitee.randomobject.domain.Payload;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;
import java.util.UUID;

/**
 * jwtutis工具类
 */
@Slf4j
public class JWTUtil {


    private static String JWT_KEY = "user";

    /**
     * 私钥加密token
     *
     * @param userInfo   待加密数据，即载荷中的数据
     * @param privateKey 私钥
     * @param expire     过期时间 /分钟
     * @return jwt
     */
    public static String generateToken(Object userInfo, PrivateKey privateKey, int expire) {

        return Jwts.builder()
                .claim(JWT_KEY, JSONObject.toJSONString(userInfo))
                .setId(createJID())
                .setExpiration(DateTime.now().plusMinutes(expire).toDate())
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    /**
     * 自定义JWT_KEY
     * @param key 自定义的key
     */
    public static void setJwtKey(String key) {
        JWT_KEY = key;
    }

    /**
     * 自定义key和value 私钥加密token
     *
     * @param keyAndValue 使用自定义的key来取加密的值
     * @param privateKey  私钥
     * @param expire      过期时间 /分钟
     * @return jwt
     */
    public static String generateToken(Map<String, Object> keyAndValue, PrivateKey privateKey, int expire) {

        if (ObjectUtil.isEmpty(keyAndValue)) {
            throw new IllegalArgumentException("自定义的keyAndValue不能为null");
        }
        JwtBuilder builder = Jwts.builder();
        keyAndValue.forEach((k, v) -> builder.claim(k, v));
        return builder.setId(createJID())
                .setExpiration(DateTime.now().plusMinutes(expire).toDate())
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }


    /**
     * 公钥解密token
     * @param token     请求体中的token
     * @param publicKey 公钥
     * @return Jws<Claims>
     */
    public static Jws<Claims> parserToken(String token, PublicKey publicKey) {
        return Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(token);
    }


    /**
     * 获取token中的(returnInfo)返回信息
     *
     * @param token     用户请求中的令牌
     * @param publicKey 公钥
     * @param classType 返回class类型
     * @param <T>
     */
    public static <T> Payload<T> getReturnInfoFromToken(String token, PublicKey publicKey, Class<T> classType) {
        //得到解密后的token值
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        //得到解密内容
        Claims body = claimsJws.getBody();
        //创建返回结果
        Payload<T> result = new Payload<>();
        result.setId(body.getId());
        result.setExpire(body.getExpiration());
        result.setReturnInfo(JSONObject.parseObject(body.get(JWT_KEY).toString(), classType));
        return result;
    }


    /**
     * 获取token中的载荷信息，不返回（returnInfo）返回信息
     *
     * @param token     用户请求中的令牌
     * @param publicKey 公钥
     * @param <T>
     * @return Payload<T> 载荷
     */
    public static <T> Payload<T> getUserInfoFromToken(String token, PublicKey publicKey) {
        //得到解密后的token值
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        //得到解密内容
        Claims body = claimsJws.getBody();
        //创建返回结果
        Payload<T> result = new Payload<>();
        result.setId(body.getId());
        result.setExpire(body.getExpiration());
        return result;
    }


    /**
     * 创建JID
     * @return String JID
     */
    public static String createJID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


}
