package com.gitee.randomobject.utils;


import com.alibaba.fastjson.JSONObject;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * 二维码工具
 */
@Slf4j
public final class QrcodeUtil {

    private static final Object CHARACTER = "UTF-8";
    private static final int BLACK = 0xFF000000;//黑色，二维码黑色方块代表1;
    private static final int WHITE = 0xFFFFFFFF;//白色，二维码白色色方块代表0;
    private static final int Qrcode_Width = 360;//默认二维码宽高度为360的正方形
    private static final int Logo_Size = 60;//默认二维码宽高度为360的正方形

    private QrcodeUtil() {
    }

    /**
     * 创建二维码
     *
     * @param map <p>传递的参数，都是key--value的形式</p></br>
     *            <p>int width 二维码宽 默认为360</p></br>
     *            <p>int height 二维码高 默认为360</p></br>
     *            <p>String savePath 图片保存路径</p></br>
     *            <p>String content 需要生成的内容</p></br>
     *            <p>String type 文件格式 例如png,jpeg,jpg等</p></br>
     *            <p>EncodeHintType {@link com.google.zxing.EncodeHintType} 纠错等级、边距等配置信息</p></br>
     */
    public static void createQrcode(Map<String, Object> map) {
        String savePath = (String) map.get("savePath");
        try {
            createQrcode(map, new FileOutputStream(savePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建二维码
     *
     * @param map <p>传递的参数，都是key--value的形式</p></br>
     *            <p>int width  360 二维码宽</p></br>
     *            <p>int height 360 二维码高</p></br>
     *            <p>String content 需要生成的内容</p></br>
     *            <p>EncodeHintType {@link com.google.zxing.EncodeHintType} 纠错等级、边距等配置信息</p></br>
     */
    public static void createQrcode(Map<String, Object> map, OutputStream out) {
        log.info("[创建二维码的参数]:map={}", JSONObject.toJSONString(map));
        try {
            if (!map.containsKey("content") || "".equalsIgnoreCase((String) map.get("content"))) {
                throw new IllegalArgumentException("content不能为空");
            }
            String content = (String) map.get("content");//二维码内容
            int width = map.containsKey("width") ? (int) map.get("width") : Qrcode_Width; // 图像宽度
            int height = map.containsKey("height") ? (int) map.get("height") : Qrcode_Width; // 图像高度
            width = width > height ? height : width;
            height = width;
            String format = (String) map.get("type");// 图像类型
            Map<EncodeHintType, Object> hints = new HashMap<>();
            //内容编码格式
            hints.put(EncodeHintType.CHARACTER_SET, CHARACTER);
            // 指定纠错等级
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            //设置二维码边的空度，非负数
            hints.put(EncodeHintType.MARGIN, 1);
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            MatrixToImageWriter.writeToStream(bitMatrix, format, out);// 输出原图片
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[异常]:错误信息={}", e.getMessage());
        }
    }


    /**
     * 创建二维码
     *
     * @param map <p>传递的参数，都是key--value的形式</p></br>
     *            <p>int width 二维码宽 默认为360</p></br>
     *            <p>int height 二维码高 默认为360</p></br>
     *            <p>String logoPath 图片保存路径</p></br>
     *            <p>int  logoSize 需要生成的内容</p></br>
     *            <p>String savePath 图片保存路径</p></br>
     *            <p>String content 需要生成的内容</p></br>
     *            <p>String type 文件格式 例如png,jpeg,jpg等</p></br>
     *            <p>EncodeHintType {@link com.google.zxing.EncodeHintType} 纠错等级、边距等配置信息</p></br>
     */
    public static void createQrcodeWithLogo(Map<String, Object> map) {

        try {
            String savePath = (String) map.get("savePath");
            if (StringUtil.checkEmpty(savePath)) {
                throw new IllegalArgumentException("savePath保存路径不能为空");
            }
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(savePath));
            createQrcodeWithLogo(map, bufferedOutputStream);
        } catch (FileNotFoundException e) {
            log.info("[异常信息]message={}", e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * 创建二维码带logo的
     *
     * @param map <p>传递的参数，都是key--value的形式</p></br>
     *            <p>int width 二维码宽 默认为360</p></br>
     *            <p>int height 二维码高 默认为360</p></br>
     *            <p>String logoPath 图片保存路径</p></br>
     *            <p>int  logoSize 需要生成的内容</p></br>
     *            <p>String savePath 图片保存路径</p></br>
     *            <p>String content 需要生成的内容</p></br>
     *            <p>String type 文件格式 例如png,jpeg,jpg等</p></br>
     *            <p>EncodeHintType {@link com.google.zxing.EncodeHintType} 纠错等级、边距等配置信息</p></br>
     */
    public static void createQrcodeWithLogo(Map<String, Object> map, OutputStream outputStream) {
        log.info("[创建二维码带logo的参数]:map={}", JSONObject.toJSONString(map));
        //得到logo图片文件
        File file = new File((String) map.get("logoPath"));
        if (!file.exists()) {
            log.info("[logo图片地址不能为空]");
            return;
        }

        Hashtable ht = new Hashtable();
        // 设置二维码纠错等级，L(7%)、M(15%)、Q(25%)、H(30%)，纠错等级可存储的信息越少，但对二维码清晰度的要求越小
        ht.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        //设置UTF-8字符编码集
        ht.put(EncodeHintType.CHARACTER_SET, CHARACTER);
        //设置图片边距
        ht.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = null;
        try {
            int width = map.containsKey("width") ? (int) map.get("width") : Qrcode_Width;
            int height = map.containsKey("height") ? (int) map.get("height") : Qrcode_Width;
            bitMatrix = new MultiFormatWriter().encode((String) map.get("content"), BarcodeFormat.QR_CODE, width,
                    height, ht);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        //使用BufferedImage生成二维码图片
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? BLACK : WHITE);
            }
        }

        int logoSize = map.containsKey("logoSize") ? (int) map.get("logoSize") : Logo_Size;
        try {
            BufferedImage logoImage = ImageIO.read(file);
            //创建画笔，画上logo图片
            Graphics2D graphics = bufferedImage.createGraphics();
            //居中放置logo图
            int x = (360 - logoSize) / 2;
            int y = (360 - logoSize) / 2;
            graphics.drawImage(logoImage, x, y, logoSize, logoSize, null);
            graphics.dispose();
            //写出
            ImageIO.write(bufferedImage, "png", outputStream);
        } catch (IOException e) {
            log.info("[异常信息]message={}", e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * 解析二维码
     *
     * @param picPath
     * @return String
     */
    public static String readQrcode(String picPath) {
        try {
            MultiFormatReader formatReader = new MultiFormatReader();
            //读取指定的二维码文件
            BufferedImage bufferedImage = ImageIO.read(new File(picPath));
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
            //定义二维码参数
            Map hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            Result result = formatReader.decode(binaryBitmap, hints);
            //输出相关的二维码信息
            log.info("[解析结果]:结果={}", result.toString());
            bufferedImage.flush();
            return result.getText();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[异常]:错误信息={}", e.getMessage());
        }
        return null;
    }


    public static void main(String[] args) {

        String s = readQrcode("C:\\Users\\nikol\\Pictures\\抖音\\解析地址.png");
        System.out.println(s);


    }

}
