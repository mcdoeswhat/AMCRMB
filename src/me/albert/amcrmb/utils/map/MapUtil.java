package me.albert.amcrmb.utils.map;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.google.common.collect.Lists;
import me.albert.amcrmb.AMCRMB;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class MapUtil {


    public static PacketContainer createWindowItems(int windowId, ItemStack[] items) {
        PacketContainer packet = AMCRMB.getProtocolManager().createPacket(PacketType.Play.Server.WINDOW_ITEMS);
        packet.getIntegers().write(0, windowId);
        packet.getItemListModifier().write(0, Lists.newArrayList(items));
        packet.setMeta("amc", true);
        return packet;
    }

    public static void OpenMap(BufferedImage image, Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(AMCRMB.getInstance(), () -> {
            MapView mapView;
            mapView = Bukkit.createMap(Objects.requireNonNull(Bukkit.getWorld("world")));
            mapView.getRenderers().clear();
            mapView.setScale(MapView.Scale.FARTHEST);
            Graphics graphics = image.createGraphics();
            graphics.drawImage(image, 0, 0, 128, 128, null);
            mapView.addRenderer(new PayRenderer(image));
            player.sendMap(mapView);
            ItemStack[] items = new ItemStack[46];
            for (int i = 0; i < 46; i++) {
                items[i] = new ItemStack(Material.AIR);
            }
            ItemStack mapItem = new ItemStack(Material.FILLED_MAP);
            MapMeta mapMeta = (MapMeta) mapItem.getItemMeta();
            mapMeta.setMapView(mapView);
            mapItem.setItemMeta(mapMeta);
            items[40] = mapItem;
            PacketContainer customMapPacket = createWindowItems(0, items);
            try {
                AMCRMB.checks.put(player.getUniqueId(), System.currentTimeMillis());
                player.getInventory().setHeldItemSlot(4);
                AMCRMB.getProtocolManager().sendServerPacket(player, customMapPacket);
                player.sendMessage("§a请使用相应的支付APP扫描二维码进行支付");
                player.sendMessage("§a支付完成后请等待数十秒即可(§c未到账重新进服即可§a)");
                player.sendMessage("§7打开背包点击地图即可退出支付");
            } catch (Exception e) {
                if (AMCRMB.getInstance().getConfig().getBoolean("debug")) {
                    e.printStackTrace();
                }
            }
        });

    }
}
