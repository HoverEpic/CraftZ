package me.JangoBrick.CraftZ.Listeners;

import me.JangoBrick.CraftZ.CraftZ;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {
	
	public BlockPlaceListener(CraftZ plugin) {
		
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPlace(BlockPlaceEvent event) {
		
		String value_world_name = plugin.getConfig().getString("Config.world.name");
		World eventWorld = event.getPlayer().getWorld();
		if (eventWorld.getName().equalsIgnoreCase(value_world_name)) {
			
			Player eventPlayer = event.getPlayer();
			Block eventBlock = event.getBlock();
			Material eventBlockType = eventBlock.getType();
			
			boolean value_blockPlacing_allow = plugin.getConfig().getBoolean("Config.players.interact.block-placing");
			if (value_blockPlacing_allow != true) {
				if (!eventPlayer.hasPermission("craftz.build")) {
					event.setCancelled(true);
				}
			}
			
			boolean value_blockPlacing_spiderweb_allow = plugin.getConfig().getBoolean("Config.players.interact.allow-spiderweb-placing");
			if (value_blockPlacing_spiderweb_allow == true) {
				if (eventBlockType == Material.WEB) {
					event.setCancelled(false);
					return;
				}
			}
			
			if (event.isCancelled()) {
				String value_notEnoughPerms = ChatColor.DARK_RED + plugin.getLangConfig()
						.getString("Messages.errors.not-enough-permissions");
				eventPlayer.sendMessage(value_notEnoughPerms);
			}
		
		}
		
	}
	
	
	
	
	private CraftZ plugin;
	
}