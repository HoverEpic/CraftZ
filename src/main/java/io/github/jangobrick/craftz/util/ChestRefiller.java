package io.github.jangobrick.craftz.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import io.github.jangobrick.craftz.CraftZ;

public class ChestRefiller {

    private static Map<String, Integer> cooldowns = new HashMap<String, Integer>();

    public static void resetAllChestsAndStartRefill() {

        if (WorldData.get().getConfigurationSection("Data.lootchests") != null)
            for (String chestEntry : WorldData.get().getConfigurationSection("Data.lootchests").getKeys(false))
                resetChestAndStartRefill(chestEntry, false);

    }

    public static boolean resetChestAndStartRefill(String chestEntry, boolean drop) {

        cooldowns.put(chestEntry, 0);

        ConfigurationSection chestSec = WorldData.get().getConfigurationSection("Data.lootchests." + chestEntry);

        if (chestSec == null)
            return false;

        int rflLocX = chestSec.getInt("coords.x");
        int rflLocY = chestSec.getInt("coords.y");
        int rflLocZ = chestSec.getInt("coords.z");
        World rflWorld = Bukkit.getWorld(ConfigManager.getConfig("config").getString("Config.world.name"));
        Location rflLoc = new Location(rflWorld, rflLocX, rflLocY, rflLocZ);

        Block block = rflLoc.getBlock();

        try { // Workaround for NPE when Bukkit calls CraftInventory.getSize()
            if (block.getState() instanceof Chest && !drop)
                ((Chest) block.getState()).getBlockInventory().clear();
        } catch (NullPointerException ex) {
        }

        block.setType(Material.AIR);

        return true;

    }

    public static void evalChestRefill(ConfigurationSection chestSec) {

        int rflLocX = chestSec.getInt("coords.x");
        int rflLocY = chestSec.getInt("coords.y");
        int rflLocZ = chestSec.getInt("coords.z");
        String sface = chestSec.getString("face", "n");
        World rflWorld = CraftZ.world();
        Location rflLoc = new Location(rflWorld, rflLocX, rflLocY, rflLocZ);

        String lootList = chestSec.getString("list");

        if (lootList != null) {

            Block block = rflLoc.getBlock();
            block.setType(Material.CHEST);
            Chest chest = (Chest) block.getState();

            BlockFace face = sface.equalsIgnoreCase("s") ? BlockFace.SOUTH : (sface.equalsIgnoreCase("e") ? BlockFace.EAST
                    : (sface.equalsIgnoreCase("w") ? BlockFace.WEST : BlockFace.NORTH));
            ((org.bukkit.material.Chest) chest.getData()).setFacingDirection(face);

            List<String> bItems = ConfigManager.getConfig("loot").getStringList("Loot.lists." + lootList);
            if (bItems == null || bItems.isEmpty())
                return;
            List<String> items = new ArrayList<String>();

            for (int e = 0; e < bItems.size(); e++) {

                String str = bItems.get(e);

                if (str.contains("x"))

                    try {
                        for (int i = 0; i <= Integer.parseInt(str.split("x", 2)[0]); i++)
                            items.add(str.split("x", 2)[1]);
                    } catch (NumberFormatException ex) {
                        continue;
                    }
                else
                    items.add(str);

            }

            int min = getPropertyInt("min-stacks-filled", lootList);
            int max = getPropertyInt("max-stacks-filled", lootList);

            for (int i = 0; i < (1 + min + new Random().nextInt(max - min)); i++) {
                String itemString = items.get(new Random().nextInt(items.size()));
                ItemStack stack = StackParser.fromString(itemString, false);
                if (stack != null)
                    chest.getInventory().addItem(stack);
            }

            ItemRenamer.convertInventoryItemNames(chest.getInventory(), ConfigManager.getConfig("config").getStringList("Config.change-item-names.names"));

        }

    }

    public static int getPropertyInt(String name, String list) {
        return ConfigManager.getConfig("loot").contains("Loot.lists-settings." + list + "." + name)
                ? ConfigManager.getConfig("loot").getInt("Loot.lists-settings." + list + "." + name)
                : ConfigManager.getConfig("loot").getInt("Loot.settings." + name);
    }

    public static void onServerTick() {

        Set<String> toRemove = new TreeSet<String>();
        Iterator<Map.Entry<String, Integer>> it = cooldowns.entrySet().iterator();

        while (it.hasNext()) {

            Map.Entry<String, Integer> entry = it.next();

            entry.setValue(entry.getValue() + 1);
            if (entry.getValue() >= (ConfigManager.getConfig("loot").getInt("Loot.settings.time-before-refill") * 20)) {

                toRemove.add(entry.getKey());
                ConfigurationSection chestSec = WorldData.get().getConfigurationSection("Data.lootchests." + entry.getKey());
                if (chestSec != null)
                    evalChestRefill(chestSec);

            }

        }

        for (String str : toRemove)
            cooldowns.remove(str);

    }

}
