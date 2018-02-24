package io.github.jangobrick.craftz.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

import io.github.jangobrick.craftz.CraftZ;
import io.github.jangobrick.craftz.util.ConfigManager;

public class PlayerBedEnterListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {

        if (CraftZ.isWorld(event.getBed().getWorld()))
            if (!ConfigManager.getConfig("config").getBoolean("Config.players.interact.sleeping") && !event.getPlayer().hasPermission("craftz.sleep"))
                event.setCancelled(true);

    }

}
