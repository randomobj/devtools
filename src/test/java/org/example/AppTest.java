package org.example;

import cn.hutool.aop.ProxyUtil;
import cn.hutool.bloomfilter.BitMapBloomFilter;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.gitee.randomobject.aspect.Man;
import com.gitee.randomobject.aspect.ManSimpleAspect;
import com.gitee.randomobject.aspect.Person;
import com.gitee.randomobject.captcha.GifCaptcha;
import com.gitee.randomobject.captcha.VerifUtil;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionService;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest {

    private final Log log = LogFactory.create().createLog(AppTest.class);

    @Test
    public void shouldAnswerWithTrue() throws FileNotFoundException {
    }

    @Test
    public void Test() {

        BitMapBloomFilter bitMapBloomFilter = new BitMapBloomFilter(30);
        System.out.println(bitMapBloomFilter.add("helloworld"));
        System.out.println(bitMapBloomFilter.contains("he"));
    }

    @Test

    public void TestHttpUtil() {

//        HttpResponse execute = HttpUtil.createRequest(Method.GET, "https://www.baidu.com").execute();
//        System.out.println(execute.body());
//        String s = HttpUtil.get("https://www.baidu.com");
////        System.out.println(s);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("pageNum", 1);
        paramMap.put("pageSize", 30);
        String post = HttpUtil.post("http://courage.cqscrb.top/admin/game/querySchoolCityLeague", paramMap);
        System.out.println(post);
        log.info("[开始测试]:你好:{}", new Date());
    }

    @Test
    public void screctTest() {
        RSA rsa = SecureUtil.rsa();
        String message = "我是一只小鸟";
        String s = rsa.encryptBase64(StrUtil.bytes(message), KeyType.PrivateKey);
        System.out.println(s);
        System.out.println(rsa.decryptStr(s, KeyType.PublicKey));
    }

    @Test
    public void createRsa() {
        KeyPair rsa = SecureUtil.generateKeyPair("RSA");
        String privateStr = Base64.encode(rsa.getPrivate().getEncoded());
        String publicStr = Base64.encode(rsa.getPublic().getEncoded());
        String message = "我是一只小鸟";
        //使用自定义的私钥加密
        RSA rsa1 = new RSA(privateStr, publicStr);
        String s = rsa1.encryptBase64(message, KeyType.PublicKey);
        System.out.println(s);
        String s1 = rsa1.decryptStr(s, KeyType.PrivateKey);
        System.out.println(s1);

    }

    @Test
    public void testMD5() {
        System.out.println(DigestUtil.md5Hex("我是一只小鸟"));

    }

    @Test
    public void testJSONUTIL() {
        String json = "{'album': '', 'fansCount': 6, 'followCount': 4, 'head': 'http://image.cqscrb.top//files/2019/5/4/46edd2527af4b0316c70cf3680d2f4fe.png', 'id': 9, 'nickName': '万里行', 'password': '123456', 'phone': '18225031312', 'project': None, 'projectKey': 'futures', 'signature': '关注产品质量，倡导品质生活', 'talkCount': 3, 'type': 0, 'uuid': 'ae4dd6cdee444a34a4b9c89dd11f9267'}";
        JSONObject jsonObject = JSONUtil.parseObj(json);
        System.out.println(jsonObject.getStr("head"));
    }

    @Test
    public void testConvert() {
        String a = "我是一个小小的可爱的字符串";
        //结果为："\\u6211\\u662f\\u4e00\\u4e2a\\u5c0f\\u5c0f\\u7684\\u53ef\\u7231\\u7684\\u5b57\\u7b26\\u4e32"
        String unicode = Convert.strToUnicode(a);
        System.out.println(unicode);
        //结果为："我是一个小小的可爱的字符串"
        String raw = Convert.unicodeToStr(unicode);
        System.out.println(raw);
    }

    @Test
    public void testThreadPool() {
        CompletionService<Object> objectCompletionService = ThreadUtil.newCompletionService();
    }

    @Test
    public void testAspect() {
        Person proxy = ProxyUtil.proxy(new Man(), ManSimpleAspect.class);
        proxy.eat("苹果");
    }

    @Test
    public void testVerifUtil() {
        GifCaptcha gif = VerifUtil.createGif(600, 150);
        System.out.println(gif.toBase64());
    }

    @Test
    public void testoperation() {
        System.out.println(2 << 3);
        System.out.println(8 >>> 1);
    }
}

