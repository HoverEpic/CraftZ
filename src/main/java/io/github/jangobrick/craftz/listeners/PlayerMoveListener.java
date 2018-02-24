package io.github.jangobrick.craftz.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import io.github.jangobrick.craftz.CraftZ;

public class PlayerMoveListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {

        if (CraftZ.isWorld(event.getPlayer().getWorld())) {

            Player p = event.getPlayer();

            if (event.getFrom().distance(event.getTo()) > 0)
                CraftZ.movingPlayers.put(p.getUniqueId(), 0);

        }

    }

}
