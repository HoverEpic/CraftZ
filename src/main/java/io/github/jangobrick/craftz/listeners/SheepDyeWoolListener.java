package io.github.jangobrick.craftz.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SheepDyeWoolEvent;

import io.github.jangobrick.craftz.CraftZ;

public class SheepDyeWoolListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSheepDyeWool(SheepDyeWoolEvent event) {

        if (CraftZ.isWorld(event.getEntity().getWorld()))
            event.setCancelled(true);

    }

}
