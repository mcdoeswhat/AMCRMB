package me.albert.amcrmb.utils.pay;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.albert.amcrmb.AMCRMB;
import me.albert.amcrmb.utils.map.MapUtil;
import me.albert.amcrmb.utils.map.QrRenderer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.util.UriEncoder;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PayUtil {
    public static void main(String[] args) {
        System.out.println((double) (0.03 * 0.03 * 0.03 * 0.03));
    }


    public static void startPay(Player player, String money, String payway) {
        String url;
        try {
            url = AMCRMB.getApi() + "/Qrpay_order?qd=" + payway + "&sid=" + AMCRMB.getSid() + "&wname=" + player.getName() + "&money=" + money;
        } catch (Exception e) {
            player.sendMessage("§c请求失败!请稍后再试");
            if (AMCRMB.getInstance().getConfig().getBoolean("debug")) {
                e.printStackTrace();
            }
            return;
        }
        String finalUrl = url;
        Bukkit.getScheduler().runTaskAsynchronously(AMCRMB.getInstance(), () -> {
            String rsp;
            try {
                rsp = getURLContent(finalUrl);
                JsonObject jo = new Gson().fromJson(rsp, JsonObject.class);
                if (jo.get("status").getAsInt() == 0) {
                    String info = jo.get("info").getAsString();
                    player.sendMessage("§c" + info);
                    return;
                }
                String payUrl = jo.get("info").getAsJsonObject().get("qr").getAsString();
                BufferedImage logo = AMCRMB.ALIPAY_IMAGE;
                if (payway.equalsIgnoreCase("qq")) {
                    logo = AMCRMB.QQ_IMAGE;
                }
                if (payway.equalsIgnoreCase("wx")) {
                    logo = AMCRMB.WX_IMAGE;
                }
                BufferedImage qrCode = QrRenderer.drawLogoQRCode(logo, payUrl);
                MapUtil.OpenMap(qrCode, player);
            } catch (Exception e) {
                player.sendMessage("§c请求失败!请稍后再试");
                if (AMCRMB.getInstance().getConfig().getBoolean("debug")) {
                    e.printStackTrace();
                }
            }
        });

    }

    public static int getPoints(String playerName) {
        String sid = AMCRMB.getSid();
        String key = AMCRMB.getKey();
        String way = "CheckMoney";
        long time = System.currentTimeMillis() / 1000;
        String md5 = getMD5Str(sid + playerName + time + key);
        String url = AMCRMB.getApi() + way + "?sign=" + md5 + "&sid=" + sid + "&wname=" + playerName + "&time=" + time;
        String result = getURLContent(url);
        try {
            JsonObject jo = new Gson().fromJson(result, JsonObject.class);
            if (jo.get("code").getAsInt() == 444) {
                return 0;
            }
            return Integer.parseInt(jo.get("data").getAsJsonArray().get(0).getAsJsonObject().get("money").getAsString());
        } catch (Exception e) {
            if (AMCRMB.getInstance().getConfig().getBoolean("debug")) {
                e.printStackTrace();
                System.out.println("§c错误Json: " + new Gson().fromJson(result, JsonObject.class));
            }
        }
        return 0;
    }

    public static boolean takePoints(String playerName, int amount) {
        String sid = AMCRMB.getSid();
        String key = AMCRMB.getKey();
        String way = "Manual";
        long time = System.currentTimeMillis() / 1000;
        String md5 = getMD5Str(sid + playerName + time + UriEncoder.encode("充值扣款") + "2" + amount + key);
        String url = AMCRMB.getApi() + way + "?sign=" + md5 + "&sid=" + sid + "&wname=" + playerName + "&time=" + time + "&text=" + UriEncoder.encode("充值扣款")
                + "&type=2&money=" + amount;
        String result = getURLContent(url);
        try {
            JsonObject jo = new Gson().fromJson(result, JsonObject.class);
            if (jo.get("code").getAsInt() == 201) {
                return true;
            }
        } catch (Exception e) {
            if (AMCRMB.getInstance().getConfig().getBoolean("debug")) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static String getURLContent(String urlStr) {
        try {
            URL e = new URL(urlStr);
            URLConnection connection = e.openConnection();
            InputStream is = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            BufferedReader bf = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while ((text = bf.readLine()) != null) {
                sb.append(text);
            }
            bf.close();
            isr.close();
            is.close();
            return sb.toString();
        } catch (Exception e) {
            if (AMCRMB.getInstance().getConfig().getBoolean("debug")) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String getMD5Str(String str) {
        byte[] digest = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            digest = md5.digest(str.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //16是表示转换为16进制数
        assert digest != null;
        return new BigInteger(1, digest).toString(16);
    }


}
