package me.JangoBrick.CraftZ;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import me.JangoBrick.CraftZ.Util.BlockChecker;
import me.JangoBrick.CraftZ.Util.EntityChecker;
import net.minecraft.server.v1_5_R2.EntityZombie;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_5_R2.entity.CraftZombie;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ZombieSpawner implements Listener {
	
	private CraftZ plugin;
	BlockChecker blockChecker;
	EntityChecker entityChecker;
	
	private Map<String, Integer> cooldowns = new HashMap<String, Integer>();
	
	public ZombieSpawner(CraftZ plugin) {
		
		this.plugin = plugin;
		blockChecker = new BlockChecker(plugin);
		entityChecker = new EntityChecker(plugin);
		
	}
	
	
	
	
	
	public void addSpawns() {
		
		if (plugin.getDataConfig().getConfigurationSection("Data.zombiespawns") != null) {
			
			for (String spawnEntry : plugin.getDataConfig().getConfigurationSection("Data.zombiespawns").getKeys(false)) {
				addSpawn(spawnEntry);
			}
			
		}
		
	}
	
	
	
	
	
	public void addSpawn(String spawn) {
		cooldowns.put(spawn, 0);
	}
	
	
	
	
	
	public EntityZombie evalZombieSpawn(ConfigurationSection spnptSec) {
		
		int spnLocX = spnptSec.getInt("coords.x");
		int spnLocY = spnptSec.getInt("coords.y");
		int spnLocZ = spnptSec.getInt("coords.z");
		World spnWorld = plugin.getServer().getWorld(plugin.getConfig().getString("Config.world.name"));
		Location spnLoc = new Location(spnWorld, spnLocX, spnLocY, spnLocZ);
		
		Location locToSpawn = blockChecker.getSafeSpawnLocationOver(spnLoc, true);
		
		int maxZombiesInRadius = spnptSec.getInt("max-zombies-in-radius");
		int maxZombiesRadius = spnptSec.getInt("max-zombies-radius");
		
		if (!entityChecker.areEntitiesNearby(locToSpawn, maxZombiesRadius, EntityType.ZOMBIE, maxZombiesInRadius)) {
			
			int zombies = 0;
			int maxZombies = plugin.getConfig().getInt("Config.mobs.zombies.spawning.maxzombies");
			
			for (Entity ent : spnWorld.getEntities()) {
				
				if (ent.getType() == EntityType.ZOMBIE) {
					zombies++;
				}
				
			}
			
			if (zombies <= maxZombies) {
				
				Entity ent = spnWorld.spawnEntity(locToSpawn, EntityType.ZOMBIE);
				EntityZombie zombie = (EntityZombie) ent;
				return zombie;
				
			} else {
				return null;
			}
			
		} else {
			return null;
		}
		
	}
	
	
	
	
	public void onServerTick() {
		
		for (String str : cooldowns.keySet()) {
			
			cooldowns.put(str, cooldowns.get(str) + 1);
			
			if (cooldowns.get(str) >= (plugin.getConfig().getInt("Config.mobs.zombies.spawning.interval") * 20)) {
				
				cooldowns.put(str, 0);
				
				
				
				Set<String> spts_zombies_set = plugin.getDataConfig()
						.getConfigurationSection("Data.zombiespawns").getKeys(false);
				
				if (spts_zombies_set != null && !spts_zombies_set.isEmpty()) {
					
					ConfigurationSection configSec = plugin.getDataConfig().getConfigurationSection("Data.zombiespawns."
							+ str);
					
					if (configSec == null) {
						return;
					}
					
					final EntityZombie spawnedZombie = evalZombieSpawn(configSec);
					
					if (spawnedZombie == null) {
						return;
					}
					
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						@Override
						public void run() {
							
							if (new Random().nextInt(7) > 0) {
								((CraftZombie) spawnedZombie.getBukkitEntity()).addPotionEffect(
										new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE,
												(new Random().nextInt(3) + 1)), false);
								((CraftZombie) spawnedZombie.getBukkitEntity()).addPotionEffect(
										new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE,
												1, false));
							} else {
								((CraftZombie) spawnedZombie.getBukkitEntity()).addPotionEffect(
										new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE,
												1, false));
								((CraftZombie) spawnedZombie.getBukkitEntity()).addPotionEffect(
										new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE,
												1, false));
							}
							
						}
					}, 10L);
					
				}
				
			}
			
		}
		
	}
	
}