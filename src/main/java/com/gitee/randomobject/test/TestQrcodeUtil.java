package com.gitee.randomobject.test;

import com.gitee.randomobject.utils.QrcodeUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName : Main
 * @Author : nikolaperelman
 * @Date: 2020-08-03 11:18
 * @Description : 测试
 */
public class TestQrcodeUtil {


    public static void main(String[] args) {
        /**
         * 创建二维码
         *
         * @param map <p></br>
         *            传递的参数，都是key--value的形式</br>
         *            int width=600 二维码宽</br>;
         *            int height=600 二维码高</br>;
         *            String savePath 图片保存路径: xxx/yyy</br>
         *            String content 需要生成的内容</br>
         *            String type 文件格式</br>
         *            EncodeHintType {@link com.google.zxing.EncodeHintType} 纠错等级、边距等配置信息</br>
         *            </p>
         * @return true or false
         */
        Map<String,Object> map = new HashMap<>();
        map.put("content", "https://www.baidu.com");
        map.put("savePath", "C:\\Users\\nikolaperelman\\Desktop\\test.png");
        map.put("logoPath", "C:\\Users\\nikolaperelman\\Pictures\\icons\\1.jpg");
        map.put("type", "png");
        map.put("logoSize", 60);
        QrcodeUtil.createQrcodeWithLogo(map);

        System.out.println(QrcodeUtil.readQrcode("C:\\Users\\nikolaperelman\\Desktop\\test.png"));
    }
}
