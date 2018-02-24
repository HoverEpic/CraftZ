package io.github.jangobrick.craftz.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCreatePortalEvent;

import io.github.jangobrick.craftz.CraftZ;
import io.github.jangobrick.craftz.util.ConfigManager;

public class EntityCreatePortalListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPortalCreate(EntityCreatePortalEvent event) {

        if (CraftZ.isWorld(event.getEntity().getWorld()))

            if (!ConfigManager.getConfig("config").getBoolean("Config.players.interact.block-placing")) {

                Player eventPlayer = (Player) event.getEntity();

                if (!eventPlayer.hasPermission("craftz.build")) {
                    event.setCancelled(true);
                    eventPlayer.sendMessage(ChatColor.DARK_RED + CraftZ.getMsg("Messages.errors.not-enough-permissions"));
                }

            }

    }

}
