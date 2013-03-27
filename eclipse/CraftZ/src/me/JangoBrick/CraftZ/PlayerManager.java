package me.JangoBrick.CraftZ;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import me.JangoBrick.CraftZ.Util.BlockChecker;
import me.JangoBrick.CraftZ.Util.ItemRenamer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
public class PlayerManager {
	
	private CraftZ plugin;
	private BlockChecker blockChecker;
	
	private HashMap<String, AdditionalCraftZData> players = new HashMap<String, AdditionalCraftZData>();
	
	public PlayerManager(CraftZ plugin) {
		this.plugin = plugin;
		blockChecker = new BlockChecker(plugin);
	}
	
	
	
	
	
	public ConfigurationSection getConfig() {
		return plugin.getDataConfig();
	}
	
	
	
	
	
	public void savePlayerToConfig(Player p) {
		
		getConfig().set("Data.players." + p.getName() + ".thirst", players.get(p.getName()).thirst);
		getConfig().set("Data.players." + p.getName() + ".zombiesKilled", players.get(p.getName()).zombiesKilled);
		getConfig().set("Data.players." + p.getName() + ".playersKilled", players.get(p.getName()).playersKilled);
		getConfig().set("Data.players." + p.getName() + ".minsSurvived", players.get(p.getName()).minutesSurvived);
		getConfig().set("Data.players." + p.getName() + ".bleeding", players.get(p.getName()).bleeding);
		getConfig().set("Data.players." + p.getName() + ".poisoned", players.get(p.getName()).poisoned);
		
		
		
		plugin.saveDataConfig();
		
	}
	
	public void loadPlayer(Player p) {
		
		if (getConfig().contains("Data.players." + p.getName())) {
			
			try {
				
				putPlayer(p, false);
				p.setLevel(players.get(p.getName()).thirst);
				
			} catch(Throwable ex) {
				
			}
			
		} else {
			
			p.setHealth(20);
			p.setFoodLevel(20);
			spawnPlayerAtRandomSpawn(p);
			
			putPlayer(p, true);
			
			savePlayerToConfig(p);
			
			p.setLevel(players.get(p.getName()).thirst);
			
		}
		
	}
	
	
	
	
	
	private void putPlayer(Player p, boolean defaults) {
		
		if (defaults) {
			
			players.put(p.getName(), new AdditionalCraftZData(20, 0, 0, 0, false, false));
			
		} else {
			
			players.put(p.getName(), new AdditionalCraftZData(getConfig().getInt("Data.players." + p.getName() + ".thirst"),
					getConfig().getInt("Data.players." + p.getName() + ".zombiesKilled"),
					getConfig().getInt("Data.players." + p.getName() + ".playersKilled"),
					getConfig().getInt("Data.players." + p.getName() + ".minsSurvived"),
					getConfig().getBoolean("Data.players." + p.getName() + ".bleeding"),
					getConfig().getBoolean("Data.players." + p.getName() + ".poisoned")));
			
		}
		
		
	}
	
	
	
	
	
	public void spawnPlayerAtRandomSpawn(Player p) {
		
		if (!plugin.getDataConfig().contains("Data.playerspawns")) {
			return;
		}
		
		Set<String> spts_players_set = plugin.getDataConfig()
				.getConfigurationSection("Data.playerspawns").getKeys(false);
		
		if (spts_players_set != null && !spts_players_set.isEmpty()) {
			
			
			Object[] spts_players = spts_players_set.toArray();
			
			int taken = new Random().nextInt(spts_players.length);
			
			ConfigurationSection configSec = plugin.getDataConfig().getConfigurationSection("Data.playerspawns."
					+ spts_players[taken].toString());
			
			if (configSec == null) {
				return;
			}
			
			int spnLocX = configSec.getInt("coords.x");
			int spnLocY = configSec.getInt("coords.y");
			int spnLocZ = configSec.getInt("coords.z");
			World spnWorld = plugin.getServer().getWorld(plugin.getConfig().getString("Config.world.name"));
			Location spnLoc = new Location(spnWorld, spnLocX, spnLocY, spnLocZ);
			
			Location locToSpawn = blockChecker.getSafeSpawnLocationOver(spnLoc, true);
			
			p.teleport(locToSpawn);
			
			p.sendMessage(ChatColor.YELLOW + plugin.getLangConfig().getString("Messages.spawned")
					.replaceAll("%s", configSec.getString("name")));
			
			
		}
		
	}
	
	
	
	
	
	public void saveAllPlayersToConfig() {
		
		for (Player p : plugin.getServer().getOnlinePlayers()) {
			savePlayerToConfig(p);
		}
		
	}
	
	
	
	
	
	public void resetPlayer(Player p) {
		
		getConfig().set("Data.players." + p.getName(), null);
		plugin.saveDataConfig();
		
	}
	
	
	
	
	
	public AdditionalCraftZData getData(String p) {
		
		if (!players.containsKey(p)) {
			loadPlayer(Bukkit.getPlayer(p));
		}
		
		return players.get(p);
		
	}
	
	
	
	
	
	public void onServerTick() {
		
		if (plugin.ticks % 1200 == 0) {
			
			for (String pn : players.keySet()) {
				
				if (players.get(pn).thirst > 0) {
					
					players.get(pn).thirst--;
					Bukkit.getPlayer(pn).setLevel(players.get(pn).thirst);
					
				} else {
					Bukkit.getPlayer(pn).damage(2);
				}
				
				players.get(pn).minutesSurvived++;
				
			}
			
		}
		
		if (plugin.ticks % 10 == 0) {
			
			for (String pn : players.keySet()) {
				
				List<String> names = plugin.getConfig().getStringList("Config.change-item-names.names");
				ItemRenamer.convertInventoryItemNames(Bukkit.getPlayer(pn).getInventory(), names);
				
			}
			
		}
		
		if (plugin.ticks % 200 == 0) {
			
			for (String pn : players.keySet()) {
				
				if (players.get(pn).bleeding) {
					Bukkit.getPlayer(pn).damage(1);
				}
				
				if (players.get(pn).poisoned) {
					Bukkit.getPlayer(pn).damage(1);
					Bukkit.getPlayer(pn).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,
							10, 1));
					Bukkit.getPlayer(pn).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,
							30, 1));
				}
				
			}
			
		}
		
	}
	
	
	
	
	
	public boolean isOutsideOfWorldRim(Player p, int radius, Location spawn) {
		
		return (p.getLocation().getBlockX() > spawn.getBlockX() + radius);
		
	}
	
}