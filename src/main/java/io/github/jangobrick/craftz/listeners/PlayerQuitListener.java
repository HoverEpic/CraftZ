package io.github.jangobrick.craftz.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import io.github.jangobrick.craftz.CraftZ;
import io.github.jangobrick.craftz.util.ConfigManager;
import io.github.jangobrick.craftz.util.PlayerManager;

public class PlayerQuitListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {

        if (CraftZ.isWorld(event.getPlayer().getWorld())) {

            if (ConfigManager.getConfig("config").getBoolean("Config.chat.modify-join-and-quit-messages"))
                event.setQuitMessage(ChatColor.RED + "Player " + event.getPlayer().getDisplayName() + " disconnected.");
            PlayerManager.savePlayerToConfig(event.getPlayer());

        }

    }

}
