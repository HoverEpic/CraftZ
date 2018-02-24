package io.github.jangobrick.craftz.listeners;

import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

import io.github.jangobrick.craftz.CraftZ;
import io.github.jangobrick.craftz.util.PlayerVisibilityBar;

public class EntityTargetLivingEntityListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityTargetLivingEntity(EntityTargetLivingEntityEvent event) {

        if (CraftZ.isWorld(event.getEntity().getWorld())) {

            if (!(event.getEntity() instanceof Zombie) || !(event.getTarget() instanceof Player))
                return;

            Zombie z = (Zombie) event.getEntity();
            Player p = (Player) event.getTarget();
            float vis = PlayerVisibilityBar.getVisibility(p);

            double blocks = 50 * vis;
            double dist = z.getLocation().distance(p.getLocation());

            if (dist > blocks)
                event.setCancelled(true);

        }

    }

}
