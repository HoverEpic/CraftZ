package io.github.jangobrick.craftz.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import io.github.jangobrick.craftz.CraftZ;
import io.github.jangobrick.craftz.util.ConfigManager;
import io.github.jangobrick.craftz.util.PlayerManager;

public class PlayerJoinListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {

        if (CraftZ.isWorld(event.getPlayer().getWorld())) {

            if (ConfigManager.getConfig("config").getBoolean("Config.chat.modify-join-and-quit-messages"))
                event.setJoinMessage(ChatColor.RED + "Player " + event.getPlayer().getDisplayName() + " connected.");

            joinPlayer(event.getPlayer());

        }

    }

    public static void joinPlayer(Player p) {

        if (PlayerManager.wasInWorld(p))
            PlayerManager.loadPlayer(p, false);
        else {

            p.setHealth(20);
            p.setFoodLevel(20);
            p.getInventory().clear();
            p.getInventory().setArmorContents(new ItemStack[]{null, null, null, null});

            p.teleport(PlayerManager.getLobby());

        }

    }

    public static class FirstTimeUse extends PlayerJoinListener {

        @Override
        @EventHandler(priority = EventPriority.HIGHEST)
        public void onPlayerJoin(PlayerJoinEvent event) {

            Player p = event.getPlayer();

            if (p.isOp()) {

                p.sendMessage("");

                for (String s : CraftZ.firstRunPlayerMessages)
                    p.sendMessage(s);

                p.sendMessage("");

            } else
                p.sendMessage("Thanks for installing CraftZ! Please take a look at the console for some important information on how to get started.");

        }

    }

}
