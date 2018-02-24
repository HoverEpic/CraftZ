package io.github.jangobrick.craftz.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSprintEvent;

import io.github.jangobrick.craftz.CraftZ;
import io.github.jangobrick.craftz.util.PlayerManager;

public class PlayerToggleSprintListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerToggleSprint(PlayerToggleSprintEvent event) {

        if (CraftZ.isWorld(event.getPlayer().getWorld())) {

            Player p = event.getPlayer();

            if (event.isSprinting() && PlayerManager.isInWorld(p) && PlayerManager.getData(p).bonesBroken)
                event.setCancelled(true);

        }

    }

}
