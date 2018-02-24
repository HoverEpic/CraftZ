package io.github.jangobrick.craftz.listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import io.github.jangobrick.craftz.CraftZ;

public class EntityShootBowListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityShootBow(EntityShootBowEvent event) {

        if (CraftZ.isWorld(event.getEntity().getWorld()))

            if (event.getEntityType() == EntityType.PLAYER) {

                Player p = (Player) event.getEntity();

                if (p.getInventory().contains(Material.TNT)) {

                    TNTPrimed tnt = p.getWorld().spawn(p.getLocation().add(0, 1, 0), TNTPrimed.class);
                    tnt.setVelocity(p.getLocation().getDirection().clone().multiply(3));
                    event.setCancelled(true);

                    if (p.getGameMode() != GameMode.CREATIVE) {

                        ItemStack firstTnt = p.getInventory().getItem(p.getInventory().first(Material.TNT));

                        if (firstTnt.getAmount() > 1)
                            firstTnt.setAmount(firstTnt.getAmount() - 1);
                        else
                            p.getInventory().setItem(p.getInventory().first(Material.TNT), new ItemStack(Material.AIR, 0));

                    }

                }

            }

    }

}
