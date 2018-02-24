package io.github.jangobrick.craftz.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.jangobrick.craftz.CraftZ;
import io.github.jangobrick.craftz.util.BlockChecker;
import io.github.jangobrick.craftz.util.ChestRefiller;
import io.github.jangobrick.craftz.util.ConfigManager;
import io.github.jangobrick.craftz.util.WorldData;
import io.github.jangobrick.craftz.util.ZombieSpawner;

public class SignChangeListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSignChange(SignChangeEvent event) {

        if (CraftZ.isWorld(event.getBlock().getWorld())) {

            String line1 = event.getLine(0);
            String line2 = event.getLine(1);
            String line3 = event.getLine(2);
            String line4 = event.getLine(3);

            Block block = event.getBlock();
            Sign sign = (Sign) block.getState();
            Player p = event.getPlayer();

            String signNotComplete = ChatColor.RED + CraftZ.getMsg("Messages.errors.sign-not-complete");
            boolean extended = ConfigManager.getConfig("config").getBoolean("Config.chat.extended-error-messages");

            ItemMeta meta = p.getItemInHand().getItemMeta();
            if (meta.getDisplayName() != null && meta.getDisplayName().startsWith(ChatColor.DARK_PURPLE + "Pre-written Sign / ")) {

                line1 = meta.getLore().get(0);
                line2 = meta.getLore().get(1);
                line3 = meta.getLore().get(2);
                line4 = meta.getLore().get(3);

                event.setLine(0, line1);
                event.setLine(1, line2);
                event.setLine(2, line3);
                event.setLine(3, line4);

            }

            if (line1.equalsIgnoreCase("[CraftZ]")) {

                if (line2.equals("")) {
                    p.sendMessage(signNotComplete);
                    if (extended)
                        p.sendMessage(ChatColor.RED + "You have to define the sign type!");
                    block.breakNaturally();
                    return;
                }

                if (line2.equalsIgnoreCase("zombiespawn"))

                    if (p.hasPermission("craftz.buildZombieSpawn")) {

                        if (line3.equals("")) {
                            p.sendMessage(signNotComplete);
                            if (extended)
                                p.sendMessage(ChatColor.RED + "Line 3 cannot be empty.");
                            block.breakNaturally();
                            return;
                        }

                        if (!line3.contains(":")) {
                            p.sendMessage(signNotComplete);
                            if (extended)
                                p.sendMessage(ChatColor.RED + "Line 3 must contain 2 values separated by a semicolon!");
                            block.breakNaturally();
                            return;
                        }

                        String maxZombiesInRadius = line3.split(":")[0];
                        String maxZombiesRadius = line3.split(":")[1];
                        int maxZombiesInRadiusI = 0;
                        int maxZombiesRadiusI = 0;
                        try {
                            maxZombiesInRadiusI = Integer.parseInt(maxZombiesInRadius);
                            maxZombiesRadiusI = Integer.parseInt(maxZombiesRadius);
                        } catch (NumberFormatException ex) {
                            p.sendMessage(signNotComplete);
                            if (extended)
                                p.sendMessage(ChatColor.RED + "One or both of the two values in line 3 are no valid integers.");
                            block.breakNaturally();
                            return;
                        }

                        Location signLoc = sign.getLocation();
                        int signLocX = (int) signLoc.getX();
                        int signLocY = (int) signLoc.getY();
                        int signLocZ = (int) signLoc.getZ();

                        String name = "x" + signLocX + "y" + signLocY + "z" + signLocZ;
                        String path = "Data.zombiespawns." + name;
                        String path_coords = path + ".coords";

                        WorldData.get().set(path_coords + ".x", signLocX);
                        WorldData.get().set(path_coords + ".y", signLocY);
                        WorldData.get().set(path_coords + ".z", signLocZ);

                        WorldData.get().set(path + ".max-zombies-in-radius", maxZombiesInRadiusI);
                        WorldData.get().set(path + ".max-zombies-radius", maxZombiesRadiusI);

                        WorldData.save();

                        p.sendMessage(ChatColor.RED + CraftZ.getMsg("Messages.successfully-created"));

                        ZombieSpawner.addSpawn(name);

                    } else
                        p.sendMessage(ChatColor.DARK_RED + CraftZ.getMsg("Messages.errors.not-enough-permissions"));

                if (line2.equalsIgnoreCase("playerspawn"))

                    if (p.hasPermission("craftz.buildPlayerSpawn")) {

                        if (line3.equals("")) {
                            p.sendMessage(signNotComplete);
                            if (extended)
                                p.sendMessage(ChatColor.RED + "Line 3 cannot be empty: you have to give the spawn point a name.");
                            block.breakNaturally();
                            return;
                        }

                        Location signLoc = sign.getLocation();
                        int signLocX = (int) signLoc.getX();
                        int signLocY = (int) signLoc.getY();
                        int signLocZ = (int) signLoc.getZ();

                        String name = "x" + signLocX + "y" + signLocY + "z" + signLocZ;
                        String path = "Data.playerspawns." + name;
                        String path_coords = path + ".coords";

                        WorldData.get().set(path_coords + ".x", signLocX);
                        WorldData.get().set(path_coords + ".y", signLocY);
                        WorldData.get().set(path_coords + ".z", signLocZ);

                        WorldData.get().set(path + ".name", line3);

                        WorldData.save();

                        p.sendMessage(ChatColor.RED + CraftZ.getMsg("Messages.successfully-created"));

                    } else
                        p.sendMessage(ChatColor.DARK_RED + CraftZ.getMsg("Messages.errors.not-enough-permissions"));

                if (line2.equalsIgnoreCase("lootchest"))

                    if (p.hasPermission("craftz.buildLootChest")) {

                        if (line3.equals("")) {
                            p.sendMessage(signNotComplete);
                            if (extended)
                                p.sendMessage(ChatColor.RED + "Line 3 cannot be empty: please put the y-coordinate (and possibly the facing) of the lootchest there.");
                            block.breakNaturally();
                            return;
                        }

                        int chestLocY = 0;
                        String[] l3spl = line3.split(":");

                        String l3y = l3spl[0];

                        if (l3y.equals("%c%")) {

                            Location bloc = block.getLocation();
                            Block b = BlockChecker.getFirst(Material.CHEST, bloc.getWorld(), bloc.getBlockX(), bloc.getBlockZ());

                            if (b == null) {
                                p.sendMessage(signNotComplete);
                                if (extended)
                                    p.sendMessage(ChatColor.RED + "No chest was found.");
                                block.breakNaturally();
                                return;
                            }

                            chestLocY = b.getY();
                            event.setLine(2, line3.replace("%c%", "" + chestLocY));

                        } else

                            try {
                                chestLocY = Integer.parseInt(l3y);
                            } catch (NumberFormatException ex) {

                                p.sendMessage(signNotComplete);
                                if (extended)
                                    p.sendMessage(ChatColor.RED + "Line 3 contains neither a correct y coordinate nor %c%");
                                block.breakNaturally();
                                return;

                            }

                        String l3f = l3spl.length > 1 ? l3spl[1] : "n";
                        if (!l3f.equalsIgnoreCase("n") && !l3f.equalsIgnoreCase("s") && !l3f.equalsIgnoreCase("e") && !l3f.equalsIgnoreCase("w")) {
                            p.sendMessage(ChatColor.RED + CraftZ.getMsg("Messages.errors.sign-facing-wrong"));
                            block.breakNaturally();
                            return;
                        }

                        String lootList = line4;
                        if (!ConfigManager.getConfig("loot").contains("Loot.lists." + lootList)) {
                            p.sendMessage(signNotComplete);
                            if (extended)
                                p.sendMessage(ChatColor.RED + "The loot list '" + lootList + "' is not defined.");
                            block.breakNaturally();
                            return;
                        }

                        Location signLoc = sign.getLocation();
                        int signLocX = (int) signLoc.getX();
                        int signLocY = (int) signLoc.getY();
                        int signLocZ = (int) signLoc.getZ();

                        String name = "x" + signLocX + "y" + signLocY + "z" + signLocZ;

                        String path = "Data.lootchests." + name;

                        WorldData.get().set(path + ".coords.x", signLocX);
                        WorldData.get().set(path + ".coords.y", chestLocY);
                        WorldData.get().set(path + ".coords.z", signLocZ);
                        WorldData.get().set(path + ".face", l3f);

                        WorldData.get().set(path + ".list", lootList);

                        WorldData.save();
                        ChestRefiller.resetChestAndStartRefill(name, false);

                        p.sendMessage(ChatColor.RED + CraftZ.getMsg("Messages.successfully-created"));

                    } else
                        p.sendMessage(ChatColor.DARK_RED + CraftZ.getMsg("Messages.errors.not-enough-permissions"));

            }

        }

    }

}
