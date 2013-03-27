package me.JangoBrick.CraftZ.Listeners;

import java.util.Random;

import me.JangoBrick.CraftZ.CraftZ;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

public class EntityDamageListener implements Listener {
	
	public EntityDamageListener(CraftZ plugin) {
		
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamage(EntityDamageEvent event) {
		
		String value_world_name = plugin.getConfig().getString("Config.world.name");
		World eventWorld = event.getEntity().getWorld();
		if (eventWorld.getName().equalsIgnoreCase(value_world_name)) {
			
			try {
				Entity eventEntity = event.getEntity();
				EntityType eventEntityType = event.getEntity().getType();
				DamageCause damageCause = event.getCause();
				
				if (eventEntityType == EntityType.ZOMBIE && damageCause == DamageCause.FIRE_TICK) {
					event.setCancelled(true);
					eventEntity.setFireTicks(0);
				} else {
					
					boolean value_mobs_blood = plugin.getConfig().getBoolean("Config.mobs.blood-particles-when-damaged");
					if (value_mobs_blood) {
						
						int bloodCount = 0;
						
						if (eventEntityType == EntityType.ZOMBIE) {
							bloodCount = event.getDamage() + new Random().nextInt(21);
						} else {
							bloodCount = event.getDamage() * 2 + new Random().nextInt(41);
						}
						
						for (int i=0; i<bloodCount; i++) {
							
							final Item blood = eventWorld.dropItemNaturally(eventEntity.getLocation(),
									new ItemStack(Material.WOOL, 1, (short) 14));
							
							blood.setPickupDelay(Integer.MAX_VALUE);
							
							plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
								@Override
								public void run() {
									blood.remove();
								}
							}, 6 + new Random().nextInt(7));
							
						}
						
					}
					
					if (eventEntityType == EntityType.PLAYER) {
						
						if (plugin.getConfig().getBoolean("Config.players.medical.bleeding.enable")) {
							
							double value_bleeding_chance = 1 - plugin.getConfig()
									.getDouble("Config.players.medical.bleeding.chance");
							if (Math.random() >= value_bleeding_chance) {
								plugin.getPlayerManager().getData(((Player) eventEntity).getName()).bleeding = true;
								((Player) eventEntity).sendMessage(ChatColor.DARK_RED + plugin.getLangConfig()
										.getString("Messages.bleeding"));
							}
							
						}
						
					}
					
				}
				
			} catch (NullPointerException e) {
				
			}
		
		}
		
	}
	
	
	
	private CraftZ plugin;
	
}
