package me.albert.amcrmb.utils.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class PayHolder implements InventoryHolder {
    private String money;

    public PayHolder(String money) {
        this.money = money;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }

    public String getMoney() {
        return money;
    }
}
