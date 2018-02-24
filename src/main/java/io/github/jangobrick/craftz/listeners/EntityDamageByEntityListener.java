package io.github.jangobrick.craftz.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.jangobrick.craftz.CraftZ;
import io.github.jangobrick.craftz.util.ConfigManager;
import io.github.jangobrick.craftz.util.PlayerManager;
import io.github.jangobrick.craftz.util.Rewarder.RewardType;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

        if (CraftZ.isWorld(event.getEntity().getWorld())) {

            if (event.getEntity() instanceof Player && PlayerManager.isInsideOfLobby((Player) event.getEntity())) {
                event.setCancelled(true);
                return;
            }

            if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {

                Player damager = (Player) event.getDamager();
                Player player = (Player) event.getEntity();

                if (damager.getItemInHand().getType() == Material.PAPER)

                    if (ConfigManager.getConfig("config").getBoolean("Config.players.medical.bleeding.heal-with-paper")) {

                        event.setCancelled(true);
                        event.setDamage(0);

                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERDRAGON_FLAP, 1, 1);
                        damager.playSound(player.getLocation(), Sound.ENTITY_ENDERDRAGON_FLAP, 1, 1);

                        if (damager.getItemInHand().getAmount() < 2)
                            damager.setItemInHand(new ItemStack(Material.AIR, 0));
                        else
                            damager.getItemInHand().setAmount(damager.getItemInHand().getAmount() - 1);

                        PlayerManager.getData(player).bleeding = false;

                        player.sendMessage(ChatColor.DARK_RED + CraftZ.getMsg("Messages.bandaged"));

                        RewardType.HEAL_PLAYER.reward(damager);

                    }

                if (damager.getItemInHand().getType() == Material.INK_SACK && damager.getItemInHand().getDurability() == 1)

                    if (ConfigManager.getConfig("config").getBoolean("Config.players.medical.healing.heal-with-rosered")) {

                        event.setCancelled(true);
                        event.setDamage(0);

                        //eventPlayer.playSound(eventPlayer.getLocation(), Sound.BREATH, 1, 1);
                        //damager.playSound(eventPlayer.getLocation(), Sound.BREATH, 1, 1);
                        if (damager.getItemInHand().getAmount() < 2)
                            damager.setItemInHand(new ItemStack(Material.AIR, 0));
                        else
                            damager.getItemInHand().setAmount(damager.getItemInHand().getAmount() - 1);

                        player.setHealth(20);

                        player.sendMessage(ChatColor.DARK_RED + CraftZ.getMsg("Messages.bloodbag"));

                        RewardType.HEAL_PLAYER.reward(damager);

                    }

                if (damager.getItemInHand().getType() == Material.INK_SACK && damager.getItemInHand().getDurability() == 10)

                    if (ConfigManager.getConfig("config").getBoolean("Config.players.medical.poisoning.cure-with-limegreen")) {

                        event.setCancelled(true);
                        event.setDamage(0);

                        player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1, 1);
                        damager.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1, 1);

                        if (damager.getItemInHand().getAmount() < 2)
                            damager.setItemInHand(new ItemStack(Material.AIR, 0));
                        else
                            damager.getItemInHand().setAmount(damager.getItemInHand().getAmount() - 1);

                        PlayerManager.getData(player).poisoned = false;

                        player.sendMessage(ChatColor.DARK_RED + CraftZ.getMsg("Messages.unpoisoned"));

                        RewardType.HEAL_PLAYER.reward(damager);

                    }

            }

            if (event.getDamager() instanceof Player && event.getEntity() instanceof Zombie) {

                Player p = (Player) event.getDamager();
                if (p.getItemInHand() != null && p.getItemInHand().hasItemMeta()) {

                    ItemMeta m = p.getItemInHand().getItemMeta();
                    if (m.hasDisplayName() && m.getDisplayName().equals(ChatColor.GOLD + "Zombie Smasher")) {

                        event.setDamage(((LivingEntity) event.getEntity()).getMaxHealth() * 10);
                        p.playSound(p.getLocation(), Sound.BLOCK_STONE_HIT, 1, 1);
                        p.playSound(p.getLocation(), Sound.BLOCK_STONE_HIT, 1, 1);
                        p.playSound(p.getLocation(), Sound.BLOCK_STONE_HIT, 1, 1);
                        p.playSound(p.getLocation(), Sound.BLOCK_STONE_HIT, 1, 1);

                    }

                }

            }

            if (event.getDamager() instanceof Zombie && event.getEntity() instanceof Player)

                if (ConfigManager.getConfig("config").getBoolean("Config.players.medical.poisoning.enable"))

                    if (Math.random() >= 1 - ConfigManager.getConfig("config").getDouble("Config.players.medical.poisoning.chance")) {

                        PlayerManager.getData((Player) event.getEntity()).poisoned = true;
                        ((Player) event.getEntity()).playSound(event.getEntity().getLocation(), Sound.ENTITY_ZOMBIE_INFECT, 1, 1);
                        ((Player) event.getEntity()).sendMessage(ChatColor.DARK_RED + CraftZ.getMsg("Messages.poisoned"));

                    }

        }

    }

}
