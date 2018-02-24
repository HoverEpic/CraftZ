package io.github.jangobrick.craftz.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;

import io.github.jangobrick.craftz.CraftZ;
import io.github.jangobrick.craftz.util.ConfigManager;

public class BlockIgniteListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockIgnite(BlockIgniteEvent event) {

        if (CraftZ.isWorld(event.getBlock().getWorld()))

            if (!ConfigManager.getConfig("config").getBoolean("Config.world.world-changing.allow-burning")) {

                Block eventBlock = event.getBlock();
                Material eventBlockType = eventBlock.getType();
                if (eventBlockType != Material.OBSIDIAN)
                    if (event.getPlayer() != null && !event.getPlayer().hasPermission("craftz.interact.blockPlace"))
                        event.setCancelled(true);
                    else
                        event.setCancelled(true);

            }

    }

}
