package me.albert.amcrmb.listeners;

import me.albert.amcrmb.AMCRMB;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Bukkit.getScheduler().runTaskAsynchronously(AMCRMB.getInstance(), () -> AMCRMB.checkPoints(e.getPlayer().getUniqueId()));
    }
}
