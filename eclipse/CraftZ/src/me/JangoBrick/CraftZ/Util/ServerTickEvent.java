package me.JangoBrick.CraftZ.Util;

import me.JangoBrick.CraftZ.CraftZ;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ServerTickEvent extends Event {
	 
	private static final HandlerList handlers = new HandlerList();
	private int ticks = 0;
	
	public ServerTickEvent(CraftZ plugin) {
		plugin.ticks = plugin.ticks + 1;
		ticks = plugin.ticks;
	}
	
	public int getTotalServerTicks() {
		return ticks;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
}
