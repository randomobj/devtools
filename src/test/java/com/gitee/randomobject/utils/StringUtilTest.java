package com.gitee.randomobject.utils;

import cn.hutool.http.HttpUtil;
import com.gitee.randomobject.captcha.GifCaptcha;
import com.gitee.randomobject.captcha.VerifUtil;
import org.junit.Test;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class StringUtilTest {

    @Test
    public void test() throws IOException {
        String s = HttpUtil.get("https://www.baidu.com");
        Path path = Paths.get("D:\\2020-04-15\\baidu\\index.txt");
        File file = path.toFile();
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        PrintWriter printWriter = new PrintWriter(file);
        printWriter.print(s);

        Scanner scanner = new Scanner(s);
        System.out.println(scanner.nextLine());

        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        bufferedWriter.write(s);

    }


    @Test
    public void createGif() throws IOException {

        GifCaptcha gif = VerifUtil.createGif(166,166);
        System.out.println(gif.text());
        FileOutputStream os = new FileOutputStream(new File("D:/test.gif"));
        gif.out(os);
//        SpecCaptcha png = VerifUtil.createPng();
//        String s = png.toBase64();
//        byte[] decode = Base64.getDecoder().decode(s);
//        os.write(decode);
//        System.out.println(s);
    }
}