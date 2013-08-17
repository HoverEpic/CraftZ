package craftZ.util;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import craftZ.CraftZ;

public class PlayerVisibilityBar {
	
	CraftZ plugin = (CraftZ) Bukkit.getPluginManager().getPlugin("CraftZ");
	
	public void updatePlayerVisibilityBar(Player p) {
		
		float visibility = 0.3F;
		
		if (p.isSneaking()) {
			if (visibility > 0.1F) {
				visibility = visibility - 0.1F;
			} else {
				visibility = 0.0F;
			}
		}
		
		if (p.isSprinting()) {
			visibility = 0.8F;
		}
		
		if (p.isInsideVehicle()) {
			visibility = 1.0F;
		}
		
		Material blockTypeAtPlayerLoc = p.getLocation().getBlock().getType();
		if (blockTypeAtPlayerLoc != Material.AIR) {
			if (visibility > 0.2F) {
				visibility = visibility - 0.15F;
			} else {
				visibility = 0.0F;
			}
		}
		
		if (p.isSleeping()) {
			visibility = 0.0F;
		}
		
		
		if (!plugin.movingPlayers.containsKey(p)) {
			if (visibility - 0.2F > 0.0F) {
				visibility = visibility - 0.2F;
			} else {
				visibility = 0.0F;
			}
		}
			
			
			
		p.setExp(visibility);
		
	}
	
	
	public float getVisibility(Player p) {
		
		float playerExp = p.getExp();
		return playerExp;
		
	}
	
}