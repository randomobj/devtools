package com.gitee.randomobject.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/***
 * RSA工具类
 */
public class RSAUtil {

    /**
     * 默认密钥对长度为2048
     */
    private static final int DEFAULT_KEY_SIZE = 2048;

    private static final String DEFAULT_ALGORITHM_KEY = "RSA";

    /**
     * <p>保存密钥对到指定的文件</p>
     *  (k,v)->{public->PublicKey,private->PrivateKey}
     * @param keyPair        密钥对
     * @param publicKeyPath  公钥文件位置
     * @param privateKeyPath 私钥文件位置
     * @throws IOException
     */
    public static Map<String, String> save2file(KeyPair keyPair, String publicKeyPath, String privateKeyPath) throws IOException {

        //得到密钥对
        byte[] publicKeyByte = keyPair.getPublic().getEncoded();
        byte[] privateKeyByte = keyPair.getPrivate().getEncoded();

        /**
         * 已经对得到的字符串密钥对进行Base64编码了，故后续的获取密钥对对象都需要先进行Base64解码
         */
        //得到公钥字符串
        String publicKeyStr = Base64.getEncoder().encodeToString(publicKeyByte);

        //得到私钥字符串
        String privateKeyStr = Base64.getEncoder().encodeToString(privateKeyByte);

        //保存公钥文件
        PrintWriter printWriter = new PrintWriter(new FileOutputStream(publicKeyPath));
        printWriter.write(publicKeyStr);
        printWriter.flush();

        //保存私钥文件
        printWriter = new PrintWriter(new FileOutputStream(privateKeyPath));
        printWriter.write(privateKeyStr);
        printWriter.flush();

        printWriter.close();

        Map<String, String> result = new HashMap<>();
        result.put("public", publicKeyStr);
        result.put("private", privateKeyStr);

        return result;
    }


    /**
     * 获取自定位置的文件内容并返回字节数组
     * @param path 文件地址
     * @return byte [] 字节数组
     */
    public static byte [] showByteKey2FilePath(String path) throws IOException {
        BufferedInputStream bufferedReader = new BufferedInputStream((new FileInputStream(path)));
        int available = bufferedReader.available();
        byte[] bytes = new byte[available];
        bufferedReader.read(bytes);
        bufferedReader.close();
        return Base64.getDecoder().decode(bytes);
    }

    /**
     * 获取自定位置的文件内容并返回字符串
     * @param path 文件地址
     * @return byte [] 字节数组
     */
    public static String showStrKey2FilePath(String path) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        Scanner scanner = new Scanner(new FileInputStream(path));
        while (scanner.hasNext()){
            stringBuilder.append(scanner.next());
        }
        scanner.close();
        return stringBuilder.toString();
    }

    /**
     * 根据公钥字节数组得到公钥对象
     * @param bytes 公钥字节数组
     * @return publicKey
     */
    public static PublicKey getPublicKey(byte [] bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec pkcs8EncodedKeySpec = new X509EncodedKeySpec(bytes);
        KeyFactory factory = KeyFactory.getInstance(DEFAULT_ALGORITHM_KEY);
        return factory.generatePublic(pkcs8EncodedKeySpec);
    }

    /**
     * 根据公钥字节数组得到公钥字符串
     * @param bytes 公钥字节数组
     * @return String
     */
    public static String getStrPublicKey(byte [] bytes) {

        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 根据私钥字节数组得到私钥对象
     * @param bytes 私钥字节数组
     * @return privateKey
     */
    public static PrivateKey getPrivateKey(byte [] bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory factory = KeyFactory.getInstance(DEFAULT_ALGORITHM_KEY);
        return factory.generatePrivate(pkcs8EncodedKeySpec);
    }

    /**
     * 根据私钥字节数组得到私钥对象
     * @param bytes 私钥字节数组
     * @return String
     */
    public static String getStrPrivateKey(byte [] bytes)  {
        return Base64.getEncoder().encodeToString(bytes);
    }


    /**
     * 根据公钥字符串得到公钥对象
     * @param publicKey 公钥字符串
     * @return PublicKey
     */
    public static PublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //Base64解码
        byte[] decode = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(decode);
        return KeyFactory.getInstance(DEFAULT_ALGORITHM_KEY).generatePublic(x509EncodedKeySpec);
    }

    /**
     * 根据私钥字符串得到私钥对象
     * @param privateKey 私钥字符串
     * @return PrivateKey
     */
    public static PrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //Base64解码
        byte[] decode = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(decode);
        return KeyFactory.getInstance(DEFAULT_ALGORITHM_KEY).generatePrivate(pkcs8EncodedKeySpec);
    }

    /**
     * 根据密文生成密钥对，并保存到指定位置
     *
     * @param publicKeyPath  公钥保存位置
     * @param privateKeyPath 私钥保存位置
     * @param secret         密文
     * @param keySize
     */
    public static Map<String, String> generateKey(String publicKeyPath, String privateKeyPath, String secret, int keySize) throws NoSuchAlgorithmException, IOException {
        return save2file(createKeyPair(secret, keySize), publicKeyPath, privateKeyPath);
    }


    /**
     * 创建密钥对
     * @param keySize 密钥长度
     * @param secret 密文
     * @return KeyPair 密钥对
     */
    public static KeyPair createKeyPair(String secret, int keySize) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(DEFAULT_ALGORITHM_KEY);
        SecureRandom secureRandom = new SecureRandom(secret.getBytes("utf-8"));
        keyPairGenerator.initialize(Math.max(keySize, DEFAULT_KEY_SIZE),secureRandom);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * 使用公钥加密
     * @param data 待加密数据
     * @param publicKey 公钥字符串
     * @return String
     */
    public static String encryptByPublicKey(String data, String publicKey) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {

        //获取公钥对象
        byte[] decode = Base64.getDecoder().decode(publicKey);
        PublicKey _publicKey = getPublicKey(decode);

        //加密对象
        Cipher cipher = Cipher.getInstance(DEFAULT_ALGORITHM_KEY);
        cipher.init(Cipher.ENCRYPT_MODE, _publicKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
    }

    /**
     * 使用公钥加密
     * @param data 待加密数据
     * @param publicKey 公钥对象
     * @return String
     */
    public static String encryptByPublicKey(String data, PublicKey publicKey) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        //加密对象
        Cipher cipher = Cipher.getInstance(DEFAULT_ALGORITHM_KEY);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
    }

    /***
     * 使用私钥解密
     * @param data 待解密的数据
     * @param privateKey 私钥字符串
     * @return String
     */
    public static String decryptByPrivateKey(String data, String privateKey) throws UnsupportedEncodingException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        //得到私钥对象
        byte[] decode = Base64.getDecoder().decode(privateKey);
        PrivateKey _privateKey = getPrivateKey(decode);

        //解密
        Cipher cipher = Cipher.getInstance(DEFAULT_ALGORITHM_KEY);
        cipher.init(Cipher.DECRYPT_MODE, _privateKey);
        return  new String(cipher.doFinal(Base64.getDecoder().decode(data.getBytes("utf-8"))));

    }

    /***
     * 使用私钥解密
     * @param data 待解密的数据
     * @param privateKey 私钥对象
     * @return String
     */
    public static String decryptByPrivateKey(String data, PrivateKey privateKey) throws UnsupportedEncodingException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        //解密
        Cipher cipher = Cipher.getInstance(DEFAULT_ALGORITHM_KEY);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return  new String(cipher.doFinal(Base64.getDecoder().decode(data.getBytes("utf-8"))));

    }

    /**
     * 使用私钥加密
     * @param data 待加密数据
     * @param privateKey 私钥字符串
     * @return String
     */
    public static String encrytByPrivateKey(String data, String privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {

        //得到私钥对象
        byte[] decode = Base64.getDecoder().decode(privateKey);
        PrivateKey _privateKey = getPrivateKey(decode);

        //加密
        Cipher cipher = Cipher.getInstance(DEFAULT_ALGORITHM_KEY);
        cipher.init(Cipher.ENCRYPT_MODE, _privateKey);

        return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes("utf-8")));
    }

    /**
     * 使用私钥加密
     * @param data 待加密数据
     * @param privateKey 私钥对象
     * @return String
     */
    public static String encrytByPrivateKey(String data, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        //加密
        Cipher cipher = Cipher.getInstance(DEFAULT_ALGORITHM_KEY);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes("utf-8")));
    }

    /**
     * 使用公钥解密
     * @param data 待解密数据
     * @param publicKey 公钥字符串
     * @return
     */
    public static String decryptByPublicKey(String data, String publicKey) throws UnsupportedEncodingException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        //得到公钥对象
        byte[] decode = Base64.getDecoder().decode(publicKey);
        PublicKey _publicKey = getPublicKey(decode);

        //解密
        Cipher cipher = Cipher.getInstance(DEFAULT_ALGORITHM_KEY);
        cipher.init(Cipher.DECRYPT_MODE,_publicKey);
        return  new String(cipher.doFinal(Base64.getDecoder().decode(data.getBytes("utf-8"))));
    }

    /**
     * 使用公钥解密
     * @param data 待解密数据
     * @param publicKey 公钥对象
     * @return
     */
    public static String decryptByPublicKey(String data, PublicKey publicKey) throws UnsupportedEncodingException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        //解密
        Cipher cipher = Cipher.getInstance(DEFAULT_ALGORITHM_KEY);
        cipher.init(Cipher.DECRYPT_MODE,publicKey);
        return  new String(cipher.doFinal(Base64.getDecoder().decode(data.getBytes("utf-8"))));
    }


}
