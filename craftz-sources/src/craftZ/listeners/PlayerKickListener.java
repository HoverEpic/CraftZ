package craftZ.listeners;


import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

import craftZ.CraftZ;
import craftZ.PlayerManager;

public class PlayerKickListener implements Listener {
	
	public PlayerKickListener(CraftZ plugin) {
		
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerKick(PlayerKickEvent event) {
		
		String value_world_name = plugin.getConfig().getString("Config.world.name");
		World eventWorld = event.getPlayer().getWorld();
		if (eventWorld.getName().equalsIgnoreCase(value_world_name)) {
			
			if (!event.getReason().startsWith("[CraftZ] ")) {
				
				PlayerManager.savePlayerToConfig(event.getPlayer());
				
				boolean value_modifyJoinQuitMessages = plugin.getConfig().getBoolean("Config.chat.modify-join-and-quit-messages");
				if (value_modifyJoinQuitMessages) {
					event.setLeaveMessage(ChatColor.RED + "Player " + event.getPlayer().getDisplayName()
							+ " disconnected.");
				}
				
			}
			
		}
		
	}
	
	
	
	
	private CraftZ plugin;
	
}