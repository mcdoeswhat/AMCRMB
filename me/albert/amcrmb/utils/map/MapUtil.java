package me.albert.amcrmb.utils.map;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.google.common.collect.Lists;
import me.albert.amcrmb.AMCRMB;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MapUtil {
    public static PacketContainer createWindowItems(int windowId, ItemStack[] items) {
        PacketContainer packet = AMCRMB.getProtocolManager().createPacket(PacketType.Play.Server.WINDOW_ITEMS);
        packet.getIntegers().write(0, windowId);
        packet.getItemListModifier().write(0, Lists.newArrayList(items));
        return packet;
    }

    public static void OpenMap(BufferedImage image, Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(AMCRMB.getInstance(), () -> {
            MapView mapView = Bukkit.getMap((short) 0);
            mapView.setScale(MapView.Scale.FARTHEST);
            mapView.getRenderers().clear();
            Graphics graphics = image.createGraphics();
            graphics.drawImage(image, 0, 0, 128, 128, null);
            mapView.addRenderer(new PayRenderer(image));
            player.sendMap(mapView);
            ItemStack[] items = new ItemStack[46];
            for (int i = 0; i < 46; i++) {
                items[i] = new ItemStack(Material.AIR);
            }
            ItemStack a = new ItemStack(Material.MAP);
            a.setDurability((short) 0);
            items[40] = a;
            PacketContainer customMapPacket = createWindowItems(0, items);
            try {
                player.getInventory().setHeldItemSlot(4);
                Thread.sleep(1000);
                AMCRMB.getProtocolManager().sendServerPacket(player, customMapPacket);
                player.sendMessage("§a请使用相应的支付APP扫描二维码进行支付");
                player.sendMessage("§a支付完成后请等待数十秒即可(重新进服可立即刷新到账)");
                player.sendMessage("§7打开背包点击地图即可退出支付");
                AMCRMB.checks.put(player.getUniqueId(),System.currentTimeMillis());
            } catch (Exception ignored) {
            }
        });

    }
}
