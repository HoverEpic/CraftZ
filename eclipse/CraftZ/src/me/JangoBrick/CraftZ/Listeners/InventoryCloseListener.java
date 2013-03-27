package me.JangoBrick.CraftZ.Listeners;

import me.JangoBrick.CraftZ.CraftZ;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCloseListener implements Listener {
	
	public InventoryCloseListener(CraftZ plugin) {
		
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryOpen(InventoryCloseEvent event) {
		
		String value_world_name = plugin.getConfig().getString("Config.world.name");
		World eventWorld = event.getPlayer().getWorld();
		if (eventWorld.getName().equalsIgnoreCase(value_world_name)) {
			
			if (event.getInventory().getHolder() instanceof Chest) {
				
				Chest chest = (Chest) event.getInventory().getHolder();
				
				for (int i=0; i<256; i++) {
					
					Block iBlock = new Location(eventWorld, chest.getLocation().getBlockX(), 
							i, chest.getLocation().getBlockZ()).getBlock();
					
					if (iBlock.getType() == Material.SIGN_POST || iBlock.getType() == Material.WALL_SIGN) {
						
						if (iBlock.getState() instanceof Sign) {
							
							if (((Sign) iBlock.getState()).getLine(2).equals("" + chest.getLocation().getBlockY())) {
								
								plugin.getChestRefiller().resetChestAndStartRefill(
										"x" + iBlock.getLocation().getBlockX() + "y" + i +
										"z" + iBlock.getLocation().getBlockZ(), true);
								
							}
							
						}
						
					}
					
				}
				
			}
			
		}
		
	}
	
	
	
	
	private CraftZ plugin;
	
}