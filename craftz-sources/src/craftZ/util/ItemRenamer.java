package craftZ.util;

import java.util.List;


import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import craftZ.CraftZ;

public class ItemRenamer {
	
	public static ItemStack rename(ItemStack input, String name, List<String> lore) {
		
		ItemMeta meta = input.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		input.setItemMeta(meta);
		
		return input;
		
	}
	
	
	
	
	
	public static void renameWithList(ItemStack input, List<String> entries) {
		
		if (!getNameFromList(input, entries).equals("")) {
			rename(input, ChatColor.RESET + getNameFromList(input, entries), null);
		}
		
	}
	
	
	
	
	
	public static String getNameFromList(ItemStack input, List<String> entries) {
		
		for (String entry : entries) {
			
			if (entry.contains("=")) {
				
				String idStr = entry.split("=")[0];
				int id = 0;
				byte meta = 0;
				
				try {
					
					if (idStr.contains(":")) {
						id = new Integer(idStr.split(":")[0]);
						meta = new Byte(idStr.split(":")[1]);
					} else {
						id = new Integer(idStr);
					}
					
				} catch (NumberFormatException ex) {
					continue;
				}
				
				if (input.getTypeId() == id && input.getData().getData() == meta) {
					return entry.split("=")[1];
				}
				
			}
			
		}
		
		return "";
		
	}
	
	
	
	
	
	public static void convertInventoryItemNames(Inventory inv, List<String> entries) {
		
		for (int i=0; i<inv.getSize(); i++) {
			
			if (inv.getItem(i) != null) {
				renameWithList(inv.getItem(i), entries);
			}
			
		}
		
	}
	
	
	
	
	
	public static void convertPlayerInventory(Player p, List<String> entries) {
		
		if (p.getWorld().getName().equalsIgnoreCase(CraftZ.instance.getConfig().getString("Config.world.name"))) {
			convertInventoryItemNames(p.getInventory(), entries);
		}
		
	}
	
}