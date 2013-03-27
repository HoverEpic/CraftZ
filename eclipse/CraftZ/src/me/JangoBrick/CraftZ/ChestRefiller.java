package me.JangoBrick.CraftZ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class ChestRefiller {
	
	private CraftZ plugin;
	private Map<String, Integer> cooldowns = new HashMap<String, Integer>();
	
	public ChestRefiller(CraftZ plugin) {
		
		this.plugin = plugin;
		
	}
	
	
	
	
	
	public void resetAllChestsAndStartRefill() {
		
		if (plugin.getDataConfig().getConfigurationSection("Data.lootchests") != null) {
			
			for (String chestEntry : plugin.getDataConfig().getConfigurationSection("Data.lootchests").getKeys(false)) {
				
				resetChestAndStartRefill(chestEntry, false);
				
			}
			
		}
		
	}
	
	
	
	
	
	public void resetChestAndStartRefill(String chestEntry, boolean drop) {
		
		cooldowns.put(chestEntry, 0);
		
		ConfigurationSection chestSec = plugin.getDataConfig()
				.getConfigurationSection("Data.lootchests." + chestEntry);
		
		int rflLocX = chestSec.getInt("coords.x");
		int rflLocY = chestSec.getInt("coords.y");
		int rflLocZ = chestSec.getInt("coords.z");
		World rflWorld = plugin.getServer().getWorld(plugin.getConfig().getString("Config.world.name"));
		Location rflLoc = new Location(rflWorld, rflLocX, rflLocY, rflLocZ);
		
		Block block = rflLoc.getBlock();
		
		if (block.getType() == Material.CHEST && !drop) {
			((Chest) block.getState()).getInventory().clear();
		}
		
		block.setType(Material.AIR);
		
	}
	
	
	
	
	
	public void evalChestRefill(ConfigurationSection chestSec) {
		
		int rflLocX = chestSec.getInt("coords.x");
		int rflLocY = chestSec.getInt("coords.y");
		int rflLocZ = chestSec.getInt("coords.z");
		World rflWorld = plugin.getServer().getWorld(plugin.getConfig().getString("Config.world.name"));
		Location rflLoc = new Location(rflWorld, rflLocX, rflLocY, rflLocZ);
		
		String lootList = chestSec.getString("list");
		
		if (lootList != null) {
			
			Block block = rflLoc.getBlock();
			block.setType(Material.CHEST);
			Chest chest = (Chest) block.getState();
			
			@SuppressWarnings("unchecked")
			List<String> bItems = (List<String>) plugin.getLootConfig().getList("Loot.lists." + lootList);
			
			if (bItems == null || bItems.isEmpty()) {
				return;
			}
			
			List<String> items = new ArrayList<String>();
			
			for (int e=0; e<bItems.size(); e++) {
				
				String str = bItems.get(e);
				
				if (str.contains("x")) {
					try {
						
						for (int i=0; i<=new Integer(str.split("x")[0]); i++) {
							items.add(str.split("x")[1]);
						}
						
					} catch(NumberFormatException ex) {
						return;
					}
				} else {
					items.add(str);
				}
				
			}
			
			for (int i=0; i<(1 + plugin.getLootConfig().getInt("Loot.settings.min-stacks-filled") + new Random().nextInt(
					plugin.getLootConfig().getInt("Loot.settings.max-stacks-filled") - plugin.getLootConfig()
					.getInt("Loot.settings.min-stacks-filled"))); i++) {
				
				String itemName = items.get(new Random().nextInt(items.size()));
				
				int itemID = 0;
				int itemData = 0;
				
				if (itemName.contains(":")) {
					
					try {
						itemID = new Integer(itemName.split(":")[0]);
						itemData = new Integer(itemName.split(":")[1]);
					} catch(NumberFormatException ex) {
						
					}
					
				} else {
					
					try {
						itemID = new Integer(itemName);
					} catch(NumberFormatException ex) {
						
					}
					
				}
				
				ItemStack itemStack = new ItemStack(itemID, 1, (short) itemData);
				
				chest.getInventory().addItem(itemStack);
				
			}
			
		}
		
	}
	
	
	
	
	
	public void onServerTick() {
		
		for (String chestKey : cooldowns.keySet()) {
			
			cooldowns.put(chestKey, cooldowns.get(chestKey) + 1);
			
			if (cooldowns.get(chestKey) >= (plugin.getLootConfig()
					.getInt("Loot.settings.time-before-refill") * 20)) {
				
				cooldowns.remove(chestKey);
				
				ConfigurationSection chestSec = plugin.getDataConfig()
						.getConfigurationSection("Data.lootchests." + chestKey);
				
				if (chestSec != null) {
					evalChestRefill(chestSec);
				}
				
			}
			
		}
		
	}
	
}