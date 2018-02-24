package io.github.jangobrick.craftz.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import io.github.jangobrick.craftz.CraftZ;
import io.github.jangobrick.craftz.util.BlockChecker;
import io.github.jangobrick.craftz.util.ConfigManager;
import io.github.jangobrick.craftz.util.PlayerManager;

public class PlayerInteractListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (CraftZ.isWorld(event.getPlayer().getWorld())) {

            Player p = event.getPlayer();
            ItemStack item = event.getItem();
            Material itemType = item != null ? item.getType() : Material.AIR;
            Action action = event.getAction();
            Block block = event.getClickedBlock();

            if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {

                if (itemType == Material.SUGAR)

                    if (ConfigManager.getConfig("config").getBoolean("Config.players.medical.enable-sugar-speed-effect") == true) {

                        if (p.getItemInHand().getAmount() < 2)
                            p.setItemInHand(new ItemStack(Material.AIR, 0));
                        else
                            p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);

                        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 3600, 2));
                        p.playSound(p.getLocation(), Sound.ENTITY_SLIME_SQUISH, 1, 1);

                    }

                if (itemType == Material.PAPER)

                    if (ConfigManager.getConfig("config").getBoolean("Config.players.medical.bleeding.heal-with-paper")) {

                        if (p.getItemInHand().getAmount() < 2)
                            p.setItemInHand(new ItemStack(Material.AIR, 0));
                        else
                            p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);

                        PlayerManager.getData(p).bleeding = false;
                        p.playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_FLAP, 1, 1);
                        p.sendMessage(ChatColor.DARK_RED + CraftZ.getMsg("Messages.bandaged"));

                    }

                if (itemType == Material.INK_SACK && item.getDurability() == 1)

                    if (ConfigManager.getConfig("config").getBoolean("Config.players.medical.healing.heal-with-rosered")
                            && !ConfigManager.getConfig("config").getBoolean("Config.players.medical.healing.only-healing-others")) {

                        if (p.getItemInHand().getAmount() < 2)
                            p.setItemInHand(new ItemStack(Material.AIR, 0));
                        else
                            p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);

                        p.setHealth(20);
                        //p.playSound(p.getLocation(), Sound.BREATH, 1, 1);
                        p.sendMessage(ChatColor.DARK_RED + CraftZ.getMsg("Messages.bloodbag"));

                    }

                if (itemType == Material.INK_SACK && item.getDurability() == 10)

                    if (ConfigManager.getConfig("config").getBoolean("Config.players.medical.poisoning.cure-with-limegreen")) {

                        if (p.getItemInHand().getAmount() < 2)
                            p.setItemInHand(new ItemStack(Material.AIR, 0));
                        else
                            p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);

                        PlayerManager.getData(p).poisoned = false;
                        p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1, 1);
                        p.sendMessage(ChatColor.DARK_RED + CraftZ.getMsg("Messages.unpoisoned"));

                    }

                if (itemType == Material.BLAZE_ROD)

                    if (ConfigManager.getConfig("config").getBoolean("Config.players.medical.bonebreak.heal-with-blazerod")) {

                        if (p.getItemInHand().getAmount() < 2)
                            p.setItemInHand(new ItemStack(Material.AIR, 0));
                        else
                            p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);

                        PlayerManager.getData(p).bonesBroken = false;
                        p.removePotionEffect(PotionEffectType.SLOW);
                        //p.playSound(p.getLocation(), Sound.BREATH, 1, 1);
                        p.sendMessage(ChatColor.DARK_RED + CraftZ.getMsg("Messages.bones-healed"));

                    }

            }

            if (action == Action.RIGHT_CLICK_BLOCK) {

                if (itemType == Material.LOG && ConfigManager.getConfig("config").getBoolean("Config.players.campfires.enable"))
                    if (!block.getType().isTransparent() && block.getType().isSolid() && block.getLocation().add(0, 1, 0).getBlock().getType() == Material.AIR) {
                        if (p.getItemInHand().getAmount() < 2)
                            p.setItemInHand(new ItemStack(Material.AIR, 0));
                        else
                            p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
                        Long campfireTicks = ConfigManager.getConfig("config").getLong("Config.players.campfires.tick-duration");
                        Vector[] fireplaceRotations = {new Vector(0, 0, 1), new Vector(1, 0, 1), new Vector(1, 0, 0), new Vector(1, 0, -1)};
                        final List<ArmorStand> fireplace = new ArrayList<ArmorStand>();
                        for (Vector rot : fireplaceRotations) {
                            ArmorStand as = (ArmorStand) block.getWorld().spawnEntity(block.getLocation().add(.5, -0.3, .5).setDirection(rot), EntityType.ARMOR_STAND);
                            as.setGravity(false);
                            as.setFireTicks(campfireTicks.intValue());
                            as.setMetadata("isFireplace", new FixedMetadataValue(CraftZ.i, true)); //in-case we need to check this in the future (food cooking?)
                            fireplace.add(as);
                        }

                        final Block torch = block.getLocation().add(0, 1, 0).getBlock();
                        torch.setType(Material.TORCH);
                        torch.setMetadata("isFireplace", new FixedMetadataValue(CraftZ.i, true));
                        Bukkit.getScheduler().scheduleSyncDelayedTask(CraftZ.i, new Runnable() {
                            public void run() {
                                torch.setType(Material.AIR);
                                for (ArmorStand as : fireplace)
                                    as.remove();
                            }
                        }, campfireTicks);

                        p.sendMessage(CraftZ.getMsg("Messages.placed-fireplace"));
                    } else
                        p.sendMessage(CraftZ.getMsg("Messages.cannot-place-fireplace"));

                if (itemType == Material.IRON_AXE)

                    if (ConfigManager.getConfig("config").getBoolean("Config.players.wood-harvesting.enable"))

                        if (BlockChecker.isTree(block))

                            if (!p.getInventory().contains(Material.LOG)) {

                                Item itm = p.getWorld().dropItem(p.getLocation(), new ItemStack(Material.LOG, 1));
                                itm.setPickupDelay(0);
                                p.sendMessage(CraftZ.getMsg("Messages.harvested-tree"));

                            } else
                                p.sendMessage(CraftZ.getMsg("Messages.already-have-wood"));
                        else
                            p.sendMessage(CraftZ.getMsg("Messages.isnt-a-tree"));

                if (itemType == Material.MINECART)

                    if (ConfigManager.getConfig("config").getBoolean("Config.vehicles.enable")) {

                        Location locForMinecart = block.getLocation();
                        locForMinecart.add(new Vector(0, 1, 0));
                        p.getWorld().spawn(locForMinecart, Minecart.class);

                        if (p.getGameMode() != GameMode.CREATIVE)
                            p.getInventory().removeItem(p.getInventory().getItemInHand());

                    }

                if (item == null)
                    return;

                ItemMeta meta = item.getItemMeta();
                if (meta.getDisplayName() != null && meta.getDisplayName().startsWith(ChatColor.DARK_PURPLE + "Pre-written Sign / ")
                        && item.getAmount() == 2 && p.getGameMode() != GameMode.CREATIVE)

                    item.setAmount(item.getAmount() + 1);

            }

            if (itemType == Material.WATCH && ConfigManager.getConfig("config").getBoolean("Config.chat.ranged.enable-radio")) {

                ItemMeta meta = item.getItemMeta();

                int channel = 0;
                try {
                    channel = Integer.parseInt(meta.getLore().get(0).replace("Channel ", ""));
                } catch (Exception ex) {
                }

                if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
                    channel++;
                else if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK)
                    channel--;

                channel = Math.max(Math.min(channel, ConfigManager.getConfig("config").getInt("Config.chat.ranged.radio-channels")), 0);

                meta.setLore(Arrays.asList("Channel " + channel));

                item.setItemMeta(meta);

            }

        }

    }

}
