package me.albert.amcrmb.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class InvUtil {
    private static ItemStack alipay = ItemUtil.make(getHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDA5YWRkNDZlMWMxZjNkMzBkOThkYjQ1NjNkNTMyNjE3MGUyZjk0ZjRlYTY5YWY2ZDJmNzc1NDk5ZTM3MGVmNCJ9fX0="), "§b支付宝");
    private static ItemStack qq = ItemUtil.make(getHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzk5MjQ2NjMyOGFlYTE4MjVhMmEzMzZiMTg5NGI3N2QxNmQ3NjE4NjI0YWFhNWZiNjRlODFkYTMxZmUyZjAifX19"), "§cQQ");
    private static ItemStack wx = ItemUtil.make(getHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWY5YTQwMWMzNzRhZDcwODNmMjVhZGNkZDUyYjQ2YTc0NzQyYzQ4YmEyZTM5ZGQ2YmMzZTAwMTAzZjJmOThkOSJ9fX0="), "§a微信");

    private static ItemStack getSkull() {
        return new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
    }

    private static ItemStack getHead(String value) {
        ItemStack skull = getSkull();
        UUID hashAsId = new UUID(value.hashCode(), value.hashCode());
        return Bukkit.getUnsafe().modifyItemStack(skull,
                "{SkullOwner:{Id:\"" + hashAsId + "\",Properties:{textures:[{Value:\"" + value + "\"}]}}}"
        );
    }

    public static void OpenPay(Player p, String money) {
        PayHolder payHolder = new PayHolder(money);
        Inventory inventory = Bukkit.createInventory(payHolder, 54, "§l请选择支付方式");
        inventory.setItem(20, alipay);
        inventory.setItem(22, wx);
        inventory.setItem(24, qq);
        p.openInventory(inventory);
    }

    public static class PayHolder implements InventoryHolder {
        private String money;

        public PayHolder(String kit) {
            this.money = kit;
        }

        @Override
        public Inventory getInventory() {
            return null;
        }

        public String getMoney() {
            return money;
        }
    }
}
