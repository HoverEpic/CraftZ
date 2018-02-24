package io.github.jangobrick.craftz.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import io.github.jangobrick.craftz.CraftZ;
import io.github.jangobrick.craftz.util.ConfigManager;

public class BlockPlaceListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {

        if (CraftZ.isWorld(event.getPlayer().getWorld())) {

            if (!ConfigManager.getConfig("config").getBoolean("Config.players.interact.block-placing") && !event.getPlayer().hasPermission("craftz.build"))
                event.setCancelled(true);

            if (ConfigManager.getConfig("config").getBoolean("Config.players.interact.allow-spiderweb-placing") && event.getBlock().getType() == Material.WEB) {
                event.setCancelled(false);
                return;
            }

        }

    }

}
