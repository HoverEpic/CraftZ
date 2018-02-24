package io.github.jangobrick.craftz.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import io.github.jangobrick.craftz.CraftZ;
import io.github.jangobrick.craftz.util.PlayerManager;

public class PlayerItemConsumeListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {

        if (CraftZ.isWorld(event.getPlayer().getWorld())) {

            Player p = event.getPlayer();
            ItemStack item = event.getItem();
            Material itemType = item != null ? item.getType() : Material.AIR;

            if (itemType == Material.POTION && item.getDurability() == 0 && PlayerManager.isInWorld(p)) {

                if (p.getItemInHand().getAmount() < 2)
                    p.setItemInHand(new ItemStack(Material.AIR, 0));
                else
                    p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);

                PlayerManager.getData(p).thirst = 20;
                p.setLevel(20);

            }

        }

    }

}
