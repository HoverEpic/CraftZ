package me.JangoBrick.CraftZ.Listeners;

import me.JangoBrick.CraftZ.CraftZ;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.vehicle.VehicleBlockCollisionEvent;

public class VehicleBlockCollisionListener implements Listener {
	
	public VehicleBlockCollisionListener(CraftZ plugin) {
		
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onVehicleBlockCollide(VehicleBlockCollisionEvent event) {
		
		String value_world_name = plugin.getConfig().getString("Config.world.name");
		World eventWorld = event.getVehicle().getWorld();
		if (eventWorld.getName().equalsIgnoreCase(value_world_name)) {
			
			boolean value_enableCars = plugin.getConfig().getBoolean("Config.vehicles.enable");
			if (value_enableCars) {
				
				Vehicle vehicle = event.getVehicle();
				Entity passenger = vehicle.getPassenger();
				if (!(passenger instanceof Player)) {
					return;
				}
				
				@SuppressWarnings("unused")
				Player player = (Player) passenger;
				Block eventBlock = event.getBlock();
				
				if ((event.getVehicle() instanceof Minecart)) {
					Minecart cart = (Minecart) vehicle;
					Block overBlock = eventBlock.getRelative(BlockFace.UP);
					Block overOverBlock = overBlock.getRelative(BlockFace.UP);
					Material overBlockMat = overBlock.getType();
					Material overOverBlockMat = overOverBlock.getType();
					
					if (overBlockMat == Material.AIR) {
						
						if (overOverBlockMat == Material.AIR) {
							Location newLoc = overBlock.getLocation();
							cart.teleport(newLoc, TeleportCause.PLUGIN);
							
						} else {
							int cartDamage = cart.getDamage();
							cart.setDamage(cartDamage + 1);
						}
						
					} else {
						int cartDamage = cart.getDamage();
						cart.setDamage(cartDamage + 1);
					}
					
				}
				
			}
		
		}
	    
	}
	
	
	
	
	private CraftZ plugin;
	
}