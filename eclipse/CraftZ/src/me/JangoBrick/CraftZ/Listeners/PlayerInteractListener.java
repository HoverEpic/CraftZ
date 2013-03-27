package me.JangoBrick.CraftZ.Listeners;

import me.JangoBrick.CraftZ.CraftZ;
import me.JangoBrick.CraftZ.Util.TreeChecker;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class PlayerInteractListener implements Listener {
	
	public PlayerInteractListener(CraftZ plugin) {
		
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent event) {
		
		String value_world_name = plugin.getConfig().getString("Config.world.name");
		World eventWorld = event.getPlayer().getWorld();
		if (eventWorld.getName().equalsIgnoreCase(value_world_name)) {
			
			//try {
				
				Player eventPlayer = event.getPlayer();
				ItemStack eventItem = event.getItem();
				Material eventItemType;
				if (eventItem != null) {
					eventItemType = eventItem.getType();
				} else {
					eventItemType = Material.AIR;
				}
				Action eventAction = event.getAction();
				Block eventBlock = event.getClickedBlock();
				
				if (eventAction == Action.RIGHT_CLICK_AIR || eventAction == Action.RIGHT_CLICK_BLOCK) {
					
					if (eventItemType == Material.SUGAR) {
						
						boolean value_enableSugarEffect = plugin.getConfig().getBoolean("Config.players.medical.enable-sugar-speed-effect");
						if (value_enableSugarEffect == true) {
							
							if (eventPlayer.getItemInHand().getAmount() < 2) {
								ItemStack airItemStack = new ItemStack(Material.AIR, 0);
								eventPlayer.setItemInHand(airItemStack);
							} else {
								eventPlayer.getItemInHand().setAmount(eventPlayer.getItemInHand().getAmount() - 1);
							}
							
							PotionEffect sugarSpeedEffect = new PotionEffect(PotionEffectType.SPEED, 3600, 2);
							eventPlayer.addPotionEffect(sugarSpeedEffect);
							eventPlayer.playSound(eventPlayer.getLocation(), Sound.BURP, 1, 1);
							
						}
						
					}
					
					
					
					if (eventItemType == Material.POTION) {
						
						if (eventItem.getDurability() == 0) {
							
							eventPlayer.setItemInHand(new ItemStack(Material.GLASS_BOTTLE, 1));
							eventPlayer.playSound(eventPlayer.getLocation(), Sound.DRINK, 1, 1);
							event.setUseItemInHand(Result.DENY);
							
							plugin.getPlayerManager().getData(eventPlayer.getName()).thirst = 20;
							eventPlayer.setLevel(20);
							
						}
						
					}
					
					
					
					if (eventItemType == Material.PAPER) {
						
						eventPlayer.playSound(eventPlayer.getLocation(), Sound.BREATH, 1, 1);
						
						if (eventPlayer.getItemInHand().getAmount() < 2) {
							ItemStack airItemStack = new ItemStack(Material.AIR, 0);
							eventPlayer.setItemInHand(airItemStack);
						} else {
							eventPlayer.getItemInHand().setAmount(eventPlayer.getItemInHand().getAmount() - 1);
						}
						
						plugin.getPlayerManager().getData(eventPlayer.getName()).bleeding = false;
						
						eventPlayer.sendMessage(ChatColor.DARK_RED + plugin.getLangConfig()
								.getString("Messages.bandaged"));
						
					}
					
				}
				
				if (eventAction == Action.RIGHT_CLICK_BLOCK) {
					
					
					
					
					if (eventItemType == Material.IRON_AXE) {
						
						TreeChecker treeChecker = new TreeChecker();
						boolean isTreeBlock = treeChecker.isTree(eventBlock);
						if (eventBlock.getType() == Material.LOG && isTreeBlock == true) {
							
							Inventory evtPlrInv = eventPlayer.getInventory();
							if (!evtPlrInv.contains(Material.LOG)) {
								
								ItemStack logFromTree = new ItemStack(Material.LOG, 1);
								evtPlrInv.addItem(logFromTree);
								String msg_harvestedTree = plugin.getLangConfig().getString("Messages.harvested-tree");
								eventPlayer.sendMessage(msg_harvestedTree);
								
							} else {
								String msg_alreadyHaveWood = plugin.getLangConfig().getString("Messages.already-have-wood");
								eventPlayer.sendMessage(msg_alreadyHaveWood);
							}
							
						} else {
							String msg_isntTree = plugin.getLangConfig().getString("Messages.isnt-a-tree");
							eventPlayer.sendMessage(msg_isntTree);
						}
						
					}
					
					
					
					if (eventItemType == Material.MINECART) {
						
						boolean value_enableCars = plugin.getConfig().getBoolean("Config.vehicles.enable");
						if (value_enableCars) {
							
							Location locForMinecart = eventBlock.getLocation();
							locForMinecart.add(new Vector(0, 1, 0));
							eventWorld.spawn(locForMinecart, Minecart.class);
							
							if (eventPlayer.getGameMode() != GameMode.CREATIVE) {
								eventPlayer.getInventory().removeItem(new ItemStack[] { event.getPlayer().getInventory().getItemInHand() });
							}
						
						}
						
					}
					
					
					
					
				}
		
			//} catch(Exception e) {
			
			//}
		
		}
		
	}
	
	
	
	private CraftZ plugin;
	
}