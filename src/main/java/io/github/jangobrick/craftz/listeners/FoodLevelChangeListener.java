package io.github.jangobrick.craftz.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import io.github.jangobrick.craftz.CraftZ;

public class FoodLevelChangeListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFoodLevelChange(FoodLevelChangeEvent event) {

        if (CraftZ.isWorld(event.getEntity().getWorld()))

            if (event.getEntityType() == EntityType.PLAYER) {

                Player p = (Player) event.getEntity();

                if (event.getFoodLevel() > p.getFoodLevel())

                    if (p.getHealth() + 2 <= p.getMaxHealth())
                        p.setHealth(p.getHealth() + 2);
                    else
                        p.setHealth(p.getMaxHealth());

            }

    }

}
