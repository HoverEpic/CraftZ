package io.github.jangobrick.craftz.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

import io.github.jangobrick.craftz.CraftZ;
import io.github.jangobrick.craftz.util.ConfigManager;
import io.github.jangobrick.craftz.util.ItemRenamer;

public class PlayerPickupItemListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {

        if (CraftZ.isWorld(event.getPlayer().getWorld()))
            ItemRenamer.convertPlayerInventory(event.getPlayer(),
                    ConfigManager.getConfig("config").getStringList("Config.change-item-names.names"));

    }

}
