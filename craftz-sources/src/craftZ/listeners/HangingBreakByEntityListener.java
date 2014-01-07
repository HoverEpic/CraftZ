package craftZ.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

import craftZ.CraftZ;
import craftZ.util.ConfigManager;


public class HangingBreakByEntityListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {
		
		if (event.getEntity().getWorld().getName().equals(CraftZ.worldName())) {
			
			if (event.getRemover().getType() == EntityType.PLAYER) {
				
				Player p = (Player) event.getRemover();
				if (!ConfigManager.getConfig("config").getBoolean("Config.players.interact.block-breaking") && !p.hasPermission("craftz.interact.blockBreak"))
					event.setCancelled(true);
				
			}
		
		}
		
	}
	
}