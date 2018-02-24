package io.github.jangobrick.craftz.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakEvent;

import io.github.jangobrick.craftz.CraftZ;

public class HangingBreakListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHangingBreak(HangingBreakEvent event) {

        if (CraftZ.isWorld(event.getEntity().getWorld())) {

            //event.setCancelled(true);
        }

    }

}
