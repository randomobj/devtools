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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码工具
 */
@Slf4j
public final class QrcodeUtil  {

//    private static final Logger log = LoggerFactory.getLogger(QrcodeUtil.class);

    /**
     *  创建二维码
     * @param map
     * <p></br>
     * 传递的参数，都是key--value的形式</br>
     * int width=600 二维码宽</br>;
     * int height=600 二维码高</br>;
     * String savePath 图片保存路径: xxx/yyy</br>
     * String content 需要生成的内容</br>
     * String type 文件格式</br>
     * EncodeHintType {@link com.google.zxing.EncodeHintType} 纠错等级、边距等配置信息</br>
     * </p>
     * @return true or false
     */
    public static boolean createQrcode(Map<String, Object> map){
        log.info("[创建二维码]:map={}", JSONObject.toJSONString(map));
        try {
            if (!map.containsKey("content")||"".equalsIgnoreCase((String) map.get("content"))){
              throw new IllegalArgumentException("content不能为空");
            }
            String content = (String) map.get("content");//二维码内容
            int width = map.containsKey("width")?(int)map.get("width"):600; // 图像宽度
            int height =map.containsKey("height")?(int)map.get("height"):600; // 图像高度
            width = width>height?height:width;
            height = width;
            String format = (String) map.get("type");// 图像类型
            if (!map.containsKey("savePath")||"".equalsIgnoreCase((String)map.get("savePath"))){
                throw new IllegalArgumentException("savePath不能为空");
            }
            String path = (String)map.get("savePath");
            path = path.contains(".")?(path.endsWith(format)?path:path.substring(0, path.indexOf("."))+"."+format):path+"."+format;
            Map<EncodeHintType, Object> hints = new HashMap<>();
            //内容编码格式
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            // 指定纠错等级
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            //设置二维码边的空度，非负数
            hints.put(EncodeHintType.MARGIN, 1);
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            MatrixToImageWriter.writeToPath(bitMatrix, format, new File(path).toPath());// 输出原图片
            return true;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            log.error("[出现异常]:错误信息={}", e.getMessage());
        } catch (WriterException e) {
            e.printStackTrace();
            log.error("[出现异常]:错误信息={}", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            log.error("[出现异常]:错误信息={}", e.getMessage());
        }
        return false;
    }

    /**
     * 创建二维码
     * @param map
     * <p></br>
     * 传递的参数，都是key--value的形式</br>
     * int width=600 二维码宽</br>;
     * int height=600 二维码高</br>;
     * String content 需要生成的内容</br>
     * EncodeHintType {@link com.google.zxing.EncodeHintType} 纠错等级、边距等配置信息</br>
     * </p>
     */
    public static void createQrcode(Map<String, Object> map,OutputStream out){
        log.info("[创建二维码]:map={}", JSONObject.toJSONString(map));
        try {
            if (!map.containsKey("content")||"".equalsIgnoreCase((String) map.get("content"))){
              throw new IllegalArgumentException("content不能为空");
            }
            String content = (String) map.get("content");//二维码内容
            int width = map.containsKey("width")?(int)map.get("width"):600; // 图像宽度
            int height =map.containsKey("height")?(int)map.get("height"):600; // 图像高度
            width = width>height?height:width;
            height = width;
            String format = (String) map.get("type");// 图像类型
            Map<EncodeHintType, Object> hints = new HashMap<>();
            //内容编码格式
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            // 指定纠错等级
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            //设置二维码边的空度，非负数
            hints.put(EncodeHintType.MARGIN, 1);
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            MatrixToImageWriter.writeToStream(bitMatrix, format, out);// 输出原图片
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            log.error("[出现异常]:错误信息={}", e.getMessage());
        } catch (WriterException e) {
            e.printStackTrace();
            log.error("[出现异常]:错误信息={}", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            log.error("[出现异常]:错误信息={}", e.getMessage());
        }
    }

    public static String readQrcode(String picPath) {
        try {
            MultiFormatReader formatReader = new MultiFormatReader();
            //读取指定的二维码文件
            BufferedImage bufferedImage = ImageIO.read(new File(picPath));
            BinaryBitmap binaryBitmap= new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
            //定义二维码参数
            Map  hints= new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            Result result = formatReader.decode(binaryBitmap, hints);
            //输出相关的二维码信息
            log.info("[解析结果]:结果={}", result.toString());
            bufferedImage.flush();
            return result.getText();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("[出现异常]:错误信息={}", e.getMessage());
        } catch (NotFoundException e) {
            e.printStackTrace();
            log.error("[出现异常]:错误信息={}", e.getMessage());
        }
        return "";
    }

}
