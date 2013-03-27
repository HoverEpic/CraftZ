package me.JangoBrick.CraftZ.Listeners;

import me.JangoBrick.CraftZ.CraftZ;

import me.JangoBrick.CraftZ.Util.ServerTickEvent;
import me.JangoBrick.CraftZ.Util.Time;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ServerTickListener implements Listener {
	
	private CraftZ plugin;
	Time time;
	
	public ServerTickListener(CraftZ plugin) {
		
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		time = new Time(plugin);
		
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onServerTick(ServerTickEvent event) {
		
		boolean value_world_realtime = plugin.getConfig().getBoolean("Config.world.real-time");
		if (value_world_realtime == true) {
			
			time.setToServerTime();
		
		}
		
	}
	
}
