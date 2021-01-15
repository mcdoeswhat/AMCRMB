package me.albert.amcrmb.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerDonateEvent extends Event {
    private int amount;
    private String player;

    private static final HandlerList handlers = new HandlerList();

    public PlayerDonateEvent(String player,int amount){
        this.amount = amount;
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }


    public int getAmount() {
        return amount;
    }

    public String getPlayer() {
        return player;
    }
}
