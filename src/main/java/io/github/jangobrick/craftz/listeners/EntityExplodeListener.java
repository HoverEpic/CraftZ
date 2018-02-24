package io.github.jangobrick.craftz.listeners;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import io.github.jangobrick.craftz.CraftZ;

public class EntityExplodeListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityExplode(EntityExplodeEvent event) {

        if (CraftZ.isWorld(event.getLocation().getWorld()))

            if (event.getEntity() != null && event.getEntityType() == EntityType.PRIMED_TNT) {

                event.setCancelled(true);

                Location eventLocation = event.getLocation();
                event.getLocation().getWorld().createExplosion(eventLocation, 0);

                List<Entity> tnt_nearbyEnts = event.getEntity().getNearbyEntities(20, 20, 20);
                for (Entity targetEntity : tnt_nearbyEnts)

                    if (targetEntity instanceof LivingEntity) {

                        LivingEntity targetLiving = (LivingEntity) targetEntity;
                        Location targetMobLoc = targetLiving.getLocation();
                        double targetDistance = eventLocation.distance(targetMobLoc) / 2;
                        targetLiving.damage(20 / targetDistance * 2);

                    }

            }

    }

}
