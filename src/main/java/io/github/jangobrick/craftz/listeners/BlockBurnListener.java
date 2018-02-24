package io.github.jangobrick.craftz.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;

import io.github.jangobrick.craftz.CraftZ;
import io.github.jangobrick.craftz.util.ConfigManager;

public class BlockBurnListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBurn(BlockBurnEvent event) {

        if (CraftZ.isWorld(event.getBlock().getWorld()))
            if (!ConfigManager.getConfig("config").getBoolean("Config.world.world-changing.allow-burning"))
                event.setCancelled(true);

    }

}
