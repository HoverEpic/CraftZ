package me.JangoBrick.CraftZ.Listeners;


import java.util.List;

import me.JangoBrick.CraftZ.CraftZ;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EntityExplodeListener implements Listener {
	
	public EntityExplodeListener(CraftZ plugin) {
		
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDropItem(EntityExplodeEvent event) {
		
		String value_world_name = plugin.getConfig().getString("Config.world.name");
		World eventWorld = event.getLocation().getWorld();
		if (eventWorld.getName().equalsIgnoreCase(value_world_name)) {
			
			try {
				
				Entity eventEntity = event.getEntity();
				EntityType eventEntityType = eventEntity.getType();
				if (eventEntityType == EntityType.PRIMED_TNT) {
					
					event.setCancelled(true);
					
					Location eventLocation = event.getLocation();
					eventWorld.createExplosion(eventLocation, 0);
					
					List<Entity> tnt_nearbyEnts = eventEntity.getNearbyEntities(40, 40, 40);
					for (Entity targetEntity : tnt_nearbyEnts) {
						if (targetEntity instanceof LivingEntity || targetEntity instanceof Player) {
							
							LivingEntity targetLiving = (LivingEntity) targetEntity;
							Location targetMobLoc = targetLiving.getLocation();
							double targetDistance = eventLocation.distance(targetMobLoc) / 2;
							int damageToMake = (int) ((40 / targetDistance) * 2);
							targetLiving.damage(damageToMake);
							
						}
					}
					
				}
				
			} catch(Exception e) {
				
			}
			
		}
		
	}
	
	
	
	
	private CraftZ plugin;
	
}