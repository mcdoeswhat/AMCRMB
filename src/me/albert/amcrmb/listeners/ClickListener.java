package me.albert.amcrmb.listeners;

import me.albert.amcrmb.utils.inventory.PayHolder;
import me.albert.amcrmb.utils.pay.PayUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ClickListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) {
            return;
        }
        if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) {
            return;
        }
        if (e.getInventory().getHolder() instanceof PayHolder) {
            e.setCancelled(true);
        }
        if (!(e.getClickedInventory().getHolder() instanceof PayHolder)) {
            return;
        }
        String payway = "";
        if (e.getSlot() == 20) {
            payway = "ali";
        }
        if (e.getSlot() == 22) {
            payway = "wx";
        }
        if (e.getSlot() == 24) {
            payway = "qq";
        }
        if (payway.equals("")) {
            return;
        }
        PayHolder holder = (PayHolder) e.getInventory().getHolder();
        String money = holder.getMoney();
        PayUtil.startPay((Player) e.getWhoClicked(), money, payway);
        e.getWhoClicked().sendMessage("§b创建支付二维码中....");
        e.getWhoClicked().closeInventory();
    }
}
