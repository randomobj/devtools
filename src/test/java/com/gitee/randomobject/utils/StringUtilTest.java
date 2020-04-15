package com.gitee.randomobject.utils;

import cn.hutool.http.HttpUtil;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import org.junit.Test;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import static org.junit.Assert.*;

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

        ByteArrayInputStream byteArrayInputStream = new ByteInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
    }
}