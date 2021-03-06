package me.albert.amcrmb;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.albert.amcrmb.events.PlayerDonateEvent;
import me.albert.amcrmb.listeners.ClickListener;
import me.albert.amcrmb.listeners.JoinListener;
import me.albert.amcrmb.utils.InvUtil;
import me.albert.amcrmb.utils.map.MapUtil;
import me.albert.amcrmb.utils.map.QrRenderer;
import me.albert.amcrmb.utils.pay.PayUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.UUID;

public class AMCRMB extends JavaPlugin {
    public static BufferedImage ALIPAY_IMAGE;
    public static BufferedImage WX_IMAGE;
    public static BufferedImage QQ_IMAGE;
    public static HashMap<UUID, Long> checks = new HashMap<>();
    private static String sid;
    private static String key;
    private static AMCRMB instance;
    private static ProtocolManager protocolManager;

    public static String getSid() {
        return sid;
    }

    public static String getKey() {
        return key;
    }

    public static AMCRMB getInstance() {
        return instance;
    }

    public static String getApi() {
        return "http://api.mcrmb.com/Api/";
    }


    public static ProtocolManager getProtocolManager() {
        return protocolManager;
    }


    @Override
    public void onEnable() {
        protocolManager = ProtocolLibrary.getProtocolManager();
        instance = this;
        saveDefaultConfig();
        loadConfig();
        Bukkit.getPluginManager().registerEvents(new ClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
        InputStream in = getClass().getResourceAsStream("/alipay.png");
        try {
            ALIPAY_IMAGE = ImageIO.read(in);
            in = getClass().getResourceAsStream("/qqpay.png");
            QQ_IMAGE = ImageIO.read(in);
            in = getClass().getResourceAsStream("/wechat.png");
            WX_IMAGE = ImageIO.read(in);
        } catch (IOException ignored) {
        }
        getLogger().info("Loaded");
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
           try {
               for (UUID uuid : checks.keySet()){
                   long passed = System.currentTimeMillis() - checks.get(uuid);
                   if (passed/1000<120){
                       checkPoints(uuid);
                   }
               }
           } catch (Exception ignored){}
        },40,40);
    }

    public static void checkPoints(UUID uuid){
        String playerName = Bukkit.getOfflinePlayer(uuid).getName();
        int points = PayUtil.getPoints(playerName);
        if (points > 0){
            boolean takePoints = PayUtil.takePoints(playerName,points);
            Player p = Bukkit.getPlayer(uuid);
            if (takePoints){
                Bukkit.getScheduler().runTask(AMCRMB.getInstance(), () -> {
                    String cmd = AMCRMB.getInstance().getConfig().getString("command")
                            .replace("%player%",playerName).replace("%points%",String.valueOf(points));
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),cmd);
                    PlayerDonateEvent playerDonateEvent = new PlayerDonateEvent(playerName,points);
                    Bukkit.getPluginManager().callEvent(playerDonateEvent);
                });
                if (p != null){
                    p.sendMessage("§a§l您充值的"+points+"点券已经发放!");
                    p.updateInventory();
                }
            }
        }
    }

    @Override
    public void onDisable() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getOpenInventory().getTopInventory() != null && p.getOpenInventory().getTopInventory().getHolder() instanceof InvUtil.PayHolder) {
                p.closeInventory();
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("amcrmb.reload")) {
                sender.sendMessage("§c你没有权限!");
                return true;
            }
            reloadConfig();
            loadConfig();
            sender.sendMessage("§a已重新载入配置文件");
            return true;
        }
        if (args.length == 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("玩家才能使用此命令!");
                return true;
            }
            try {
                Integer.parseInt(args[0]);
            } catch (Exception ignored) {
                sender.sendMessage("§c请输入整数!");
                return true;
            }
            Player p = (Player) sender;
            InvUtil.OpenPay(p, args[0]);
            return true;
        }
        sender.sendMessage("§a/amc reload --重新载入");
        sender.sendMessage("§a/amc 金额 -- 充值点券");
        return true;
    }

    private void loadConfig() {
        sid = getConfig().getString("server.sid");
        key = getConfig().getString("server.key");
    }
}
