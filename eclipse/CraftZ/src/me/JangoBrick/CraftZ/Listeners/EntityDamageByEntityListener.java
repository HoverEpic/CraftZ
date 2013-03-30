package me.JangoBrick.CraftZ.Listeners;

import me.JangoBrick.CraftZ.CraftZ;
import me.JangoBrick.CraftZ.PlayerManager;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

public class EntityDamageByEntityListener implements Listener {
	
	public EntityDamageByEntityListener(CraftZ plugin) {
		
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		
		String value_world_name = plugin.getConfig().getString("Config.world.name");
		World eventWorld = event.getEntity().getWorld();
		if (eventWorld.getName().equalsIgnoreCase(value_world_name)) {
			
			if (event.getEntity() instanceof Player && PlayerManager.isInsideOfLobby((Player) event.getEntity())) {
				event.setCancelled(true);
				return;
			}
			
			if (event.getDamager() instanceof Player && event.getEntity() instanceof Player
					&& event.getCause() == DamageCause.ENTITY_ATTACK) {
				
				Player damager = (Player) event.getDamager();
				Player eventPlayer = (Player) event.getEntity();
				
				if (damager.getItemInHand().getType() == Material.PAPER) {
					
					if (plugin.getConfig().getBoolean("Config.players.medical.bleeding.heal-with-paper")) {
						
						event.setCancelled(true);
						event.setDamage(0);
						
						eventPlayer.playSound(eventPlayer.getLocation(), Sound.ENDERDRAGON_WINGS, 1, 1);
						damager.playSound(eventPlayer.getLocation(), Sound.ENDERDRAGON_WINGS, 1, 1);
						
						if (damager.getItemInHand().getAmount() < 2) {
							damager.setItemInHand(new ItemStack(Material.AIR, 0));
						} else {
							damager.getItemInHand().setAmount(damager.getItemInHand().getAmount() - 1);
						}
						
						PlayerManager.getData(eventPlayer.getName()).bleeding = false;
						
						eventPlayer.sendMessage(ChatColor.DARK_RED + plugin.getLangConfig()
								.getString("Messages.bandaged"));
						
					}
					
				}
				
				
				
				if (damager.getItemInHand().getType() == Material.INK_SACK
						&& damager.getItemInHand().getDurability() == 1) {
					
					if (plugin.getConfig().getBoolean("Config.players.medical.healing.heal-with-rosered")) {
						
						event.setCancelled(true);
						event.setDamage(0);
						
						eventPlayer.playSound(eventPlayer.getLocation(), Sound.BREATH, 1, 1);
						damager.playSound(eventPlayer.getLocation(), Sound.BREATH, 1, 1);
						
						if (damager.getItemInHand().getAmount() < 2) {
							damager.setItemInHand(new ItemStack(Material.AIR, 0));
						} else {
							damager.getItemInHand().setAmount(damager.getItemInHand().getAmount() - 1);
						}
						
						eventPlayer.setHealth(20);
						
						eventPlayer.sendMessage(ChatColor.DARK_RED + plugin.getLangConfig()
								.getString("Messages.bloodbag"));
						
					}
					
				}
				
				
				
				if (damager.getItemInHand().getType() == Material.INK_SACK
						&& damager.getItemInHand().getDurability() == 10) {
					
					if (plugin.getConfig().getBoolean("Config.players.medical.poisoning.cure-with-limegreen")) {
						
						event.setCancelled(true);
						event.setDamage(0);
						
						eventPlayer.playSound(eventPlayer.getLocation(), Sound.ZOMBIE_UNFECT, 1, 1);
						damager.playSound(eventPlayer.getLocation(), Sound.ZOMBIE_UNFECT, 1, 1);
						
						if (damager.getItemInHand().getAmount() < 2) {
							damager.setItemInHand(new ItemStack(Material.AIR, 0));
						} else {
							damager.getItemInHand().setAmount(damager.getItemInHand().getAmount() - 1);
						}
						
						PlayerManager.getData(eventPlayer.getName()).poisoned = false;
						
						eventPlayer.sendMessage(ChatColor.DARK_RED + plugin.getLangConfig()
								.getString("Messages.unpoisoned"));
						
					}
					
				}
				
			}
			
			
			
			if (event.getDamager() instanceof Zombie && event.getEntity() instanceof Player) {
				
				if (plugin.getConfig().getBoolean("Config.players.medical.poisoning.enable")) {
					
					double value_poisoning_chance = 1 - plugin.getConfig()
							.getDouble("Config.players.medical.poisoning.chance");
					if (Math.random() >= value_poisoning_chance) {
						
						PlayerManager.getData(((Player) event.getEntity()).getName()).poisoned = true;
						((Player) event.getEntity()).playSound(event.getEntity().getLocation(),
								Sound.ZOMBIE_INFECT, 1, 1);
						((Player) event.getEntity()).sendMessage(ChatColor.DARK_RED + plugin.getLangConfig()
								.getString("Messages.poisoned"));
						
					}
					
				}
				
			}
		
		}
		
	}
	
	
	
	private CraftZ plugin;
	
}