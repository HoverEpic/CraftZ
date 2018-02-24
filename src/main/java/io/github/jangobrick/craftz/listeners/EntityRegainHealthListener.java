package io.github.jangobrick.craftz.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

import io.github.jangobrick.craftz.CraftZ;

public class EntityRegainHealthListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {

        if (CraftZ.isWorld(event.getEntity().getWorld()))

            if (event.getEntityType() == EntityType.PLAYER)
                event.setCancelled(event.getRegainReason() == RegainReason.SATIATED);

    }

}
