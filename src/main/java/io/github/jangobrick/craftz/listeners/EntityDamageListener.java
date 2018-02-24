package io.github.jangobrick.craftz.listeners;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import io.github.jangobrick.craftz.CraftZ;
import io.github.jangobrick.craftz.util.ConfigManager;
import io.github.jangobrick.craftz.util.PlayerManager;


public class EntityDamageListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamage(EntityDamageEvent event) {
		
		if (event.getEntityType() == EntityType.PLAYER && PlayerManager.isInsideOfLobby((Player) event.getEntity())) {
			event.setCancelled(true);
			return;
		}
		
		
		
		if (CraftZ.isWorld(event.getEntity().getWorld())) {
			
			if (event.getEntityType() == EntityType.ZOMBIE && event.getCause() == DamageCause.FIRE_TICK) {
				event.setCancelled(true);
				event.getEntity().setFireTicks(0);
			} else {
				
				if (event.getEntityType() == EntityType.PLAYER) {
					
					Player p = (Player) event.getEntity();
					
					if (PlayerManager.isNotPlaying(p.getUniqueId())) {
						event.setCancelled(true);
						return;
					}
					
					if (ConfigManager.getConfig("config").getBoolean("Config.players.medical.bleeding.enable")
							&& p.getGameMode() != GameMode.CREATIVE && !event.isCancelled()) {
						
						if (Math.random() >= 1 - ConfigManager.getConfig("config").getDouble("Config.players.medical.bleeding.chance")) {
							PlayerManager.getData(p).bleeding = true;
							p.sendMessage(ChatColor.DARK_RED + CraftZ.getMsg("Messages.bleeding"));
						}
						
					}
					
					int height = (int) (event.getDamage() + 3);
					if (event.getCause() == DamageCause.FALL && ConfigManager.getConfig("config").getBoolean("Config.players.medical.bonebreak.enable")
							&& height >= ConfigManager.getConfig("config").getInt("Config.players.medical.bonebreak.height")) {
						PlayerManager.getData(p).bonesBroken = true;
						p.sendMessage(ChatColor.DARK_RED + CraftZ.getMsg("Messages.bones-broken"));
					}
					
				}
                if (event.getEntityType() == EntityType.ARMOR_STAND && event.getCause() == DamageCause.FIRE_TICK) {
                    event.setCancelled(true);       // prevent armor stand from burning away before it's "done"
                }
//				boolean value_mobs_blood = false;//ConfigManager.getConfig("config").getBoolean("Config.mobs.blood-particles-when-damaged");
//				if (!event.isCancelled() && value_mobs_blood) {
//					
//					if (!eventEntityType.isAlive() || (eventEntityType == EntityType.PLAYER
//							&& ((Player) eventEntity).getGameMode() == GameMode.CREATIVE)) {
//						return;
//					}
//					
//					int bloodCount = 0;
//					
//					if (eventEntityType == EntityType.ZOMBIE)
//						bloodCount = (int) (event.getDamage() * 2);
//					else
//						bloodCount = (int) (event.getDamage() * 6);
//					
//					for (int i=0; i<bloodCount; i++) {
//						
////						eventWorld.playEffect(eventEntity.getLocation(), Effect.STEP_SOUND,
////								Material.REDSTONE_WIRE.getId());
//						
//						final Item blood = eventWorld.dropItemNaturally(eventEntity.getLocation(), new ItemStack(Material.WOOL, 1, (short) 14));
//						
//						blood.setPickupDelay(Integer.MAX_VALUE);
//						
//						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
//							
//							@Override
//							public void run() {
//								blood.remove();
//							}
//							
//						}, 1 + new Random().nextInt(6));
//						
//					}
//					
//				}
				
			}
		
		}
		
	}
	
}