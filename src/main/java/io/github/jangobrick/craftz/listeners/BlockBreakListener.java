package io.github.jangobrick.craftz.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import io.github.jangobrick.craftz.CraftZ;
import io.github.jangobrick.craftz.util.ChestRefiller;
import io.github.jangobrick.craftz.util.ConfigManager;
import io.github.jangobrick.craftz.util.WorldData;

public class BlockBreakListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event) {
		
		if (CraftZ.isWorld(event.getPlayer().getWorld())) {
			
			Player p = event.getPlayer();
			
			if (!ConfigManager.getConfig("config").getBoolean("Config.players.interact.block-breaking")) {
				
				if (!p.hasPermission("craftz.build")) {
					
					if (!ConfigManager.getConfig("config").getBoolean("Config.players.interact.allow-spiderweb-placing")
							|| event.getBlock().getType() != Material.WEB || p.getItemInHand() == null
							|| p.getItemInHand().getType() != Material.SHEARS) {
						event.setCancelled(true);
						return;
					}
					
				} else {
					event.setExpToDrop(0);
				}
				
			} else {
				event.setExpToDrop(0);
			}
			
			
			
			if (event.getBlock().getType() == Material.SIGN_POST || event.getBlock().getType() == Material.WALL_SIGN) {
				
				Sign sign = (Sign) event.getBlock().getState();
				
				if (!sign.getLine(0).equalsIgnoreCase("[CraftZ]"))
					return;
				
				Location signLoc = sign.getLocation();
				int signLocX = (int) signLoc.getX();
				int signLocY = (int) signLoc.getY();
				int signLocZ = (int) signLoc.getZ();
				
				if (sign.getLine(1).equalsIgnoreCase("zombiespawn")) {
					
					if (p.hasPermission("craftz.buildZombieSpawn")) {
						
						WorldData.get().set("Data.zombiespawns.x" + signLocX + "y" + signLocY + "z" + signLocZ, null);
						WorldData.save();
						
						p.sendMessage(ChatColor.RED + CraftZ.getMsg("Messages.destroyed-sign"));
						
					} else {
						event.getPlayer().sendMessage(ChatColor.DARK_RED + CraftZ.getMsg("Messages.errors.not-enough-permissions"));
					}
					
				}
				
				
				
				if (sign.getLine(1).equalsIgnoreCase("playerspawn")) {
					
					if (event.getPlayer().hasPermission("craftz.buildPlayerSpawn")) {
						
						WorldData.get().set("Data.playerspawns.x" + signLocX + "y" + signLocY + "z" + signLocZ, null);
						WorldData.save();
						
						event.getPlayer().sendMessage(ChatColor.RED + CraftZ.getMsg("Messages.destroyed-sign"));
						
					} else {
						event.getPlayer().sendMessage(ChatColor.DARK_RED + CraftZ.getMsg("Messages.errors.not-enough-permissions"));
					}
					
				}
				
				
				
				if (sign.getLine(1).equalsIgnoreCase("lootchest")) {
					
					if (event.getPlayer().hasPermission("craftz.buildLootChest")) {
						
						WorldData.get().set("Data.lootchests.x" + signLocX + "y" + signLocY + "z" + signLocZ, null);
						WorldData.save();
						
						event.getPlayer().sendMessage(ChatColor.RED + CraftZ.getMsg("Messages.destroyed-sign"));
						
					} else {
						event.getPlayer().sendMessage(ChatColor.DARK_RED + CraftZ.getMsg("Messages.errors.not-enough-permissions"));
					}
					
				}
				
			}
			
			
			
			if (!event.isCancelled()) {
				
				if (event.getBlock().getType() == Material.CHEST) {
					
					Chest chest = (Chest) event.getBlock().getState();
					
					for (int i = 0; i < 256; i++) {
						
						Block iBlock = new Location(event.getPlayer().getWorld(), chest.getLocation().getBlockX(), i, chest.getLocation().getBlockZ())
								.getBlock();
						
						if (iBlock.getType() == Material.SIGN_POST || iBlock.getType() == Material.WALL_SIGN) {
							
							if (iBlock.getState() instanceof Sign && ((Sign) iBlock.getState()).getLine(2).equals("" + chest.getLocation().getBlockY())) {
								ChestRefiller.resetChestAndStartRefill("x" + iBlock.getLocation().getBlockX() + "y" + i + "z"
										+ iBlock.getLocation().getBlockZ(), true);
								
							}
							
						}
						
					}
					
				}
				
			}
			
		}
		
	}
	
}