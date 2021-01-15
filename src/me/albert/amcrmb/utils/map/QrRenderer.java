package me.albert.amcrmb.utils.map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.ByteMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import me.albert.amcrmb.AMCRMB;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;

public class QrRenderer {
    private static final int QRCOLOR = 0xFF000000; // 默认是黑色
    private static final int BGWHITE = 0xFFFFFFFF; // 背景颜色

    private static final int WIDTH = 128; // 二维码宽
    private static final int HEIGHT = 128; // 二维码高

    // 用于设置QR二维码参数
    private static Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>() {
        private static final long serialVersionUID = 1L;

        {
            put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);// 设置QR二维码的纠错级别（H为最高级别）具体级别信息
            put(EncodeHintType.CHARACTER_SET, "utf-8");// 设置编码方式
        }
    };

    public static void main(String[] args) throws WriterException {
        File logoFile = new File("A://1.png");
        String url = "https://www.baidu.com/";
        String note = "访问百度连接";
    }


    // 生成带logo的二维码图片
    public static BufferedImage drawLogoQRCode(BufferedImage logo, String qrUrl) {
        try {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            // 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
            ByteMatrix bm = multiFormatWriter.encode(qrUrl, BarcodeFormat.QR_CODE, 128, 128, hints);
            BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

            // 开始利用二维码数据创建Bitmap图片，分别设为黑（0xFFFFFFFF）白（0xFF000000）两色
            for (int x = 0; x < WIDTH; x++) {
                for (int y = 0; y < HEIGHT; y++) {
                    image.setRGB(x, y, bm.get(x, y) % 2 == 0 ? QRCOLOR : BGWHITE);
                }
            }
            int width = image.getWidth();
            int height = image.getHeight();
            // 构建绘图对象
            Graphics2D g = image.createGraphics();
            // 读取Logo图片
            // 开始绘制logo图片
            g.drawImage(logo, (int) (width * 2 / 4.5), (int) (height * 2 / 4.5), width * 2 / 16, height * 2 / 16, null);
            g.dispose();
            logo.flush();
            image.flush();
            return image;
        } catch (Exception e) {
            if (AMCRMB.getInstance().getConfig().getBoolean("debug")) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
