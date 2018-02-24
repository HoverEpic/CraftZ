package io.github.jangobrick.craftz.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;

import io.github.jangobrick.craftz.CraftZ;
import io.github.jangobrick.craftz.util.ConfigManager;

public class ShearEntityListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerShearEntity(PlayerShearEntityEvent event) {

        if (CraftZ.isWorld(event.getEntity().getWorld()))

            if (!ConfigManager.getConfig("config").getBoolean("Config.animals.shearing") && !event.getPlayer().hasPermission("craftz.admin"))
                event.setCancelled(true);

    }

}
