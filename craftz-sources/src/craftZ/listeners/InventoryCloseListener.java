package craftZ.listeners;

import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import craftZ.CraftZ;
import craftZ.util.ChestRefiller;


public class InventoryCloseListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClose(InventoryCloseEvent event) {
		
		if (event.getPlayer().getWorld().getName().equals(CraftZ.worldName())) {
			
			if (event.getInventory().getHolder() instanceof Chest) {
				
				Chest chest = (Chest) event.getInventory().getHolder();
				int y = chest.getLocation().getBlockY();
				
				for (int i=0; i<256; i++) {
					
					Location loc = new Location(chest.getWorld(), chest.getLocation().getBlockX(), i, chest.getLocation().getBlockZ());
					if (loc.getBlock().getState() instanceof Sign && ((Sign) loc.getBlock().getState()).getLine(2).equals("" + y))
						ChestRefiller.resetChestAndStartRefill("x" + loc.getBlockX() + "y" + i + "z" + loc.getBlockZ(), true);
					
				}
				
			}
			
		}
		
	}
	
}
