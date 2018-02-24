package io.github.jangobrick.craftz.util;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.jangobrick.craftz.CraftZ;

public class ItemRenamer {

    public static ItemStack rename(ItemStack input, String name, List<String> lore) {

        ItemMeta meta = input.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        input.setItemMeta(meta);

        return input;

    }

    public static void renameWithList(ItemStack input, List<String> entries) {
        if (!getNameFromList(input, entries).equals(""))
            rename(input, ChatColor.RESET + getNameFromList(input, entries), null);
    }

    @SuppressWarnings("deprecation")
    public static String getNameFromList(ItemStack input, List<String> entries) {

        for (String entry : entries)

            if (entry.contains("=")) {

                String idStr = entry.split("=")[0];
                Material mat = Material.AIR;
                short data = 0;

                try {

                    if (idStr.contains(":")) {
                        Material mat1 = Material.matchMaterial(idStr.split(":")[0]);
                        mat = mat1 != null ? mat1 : Material.getMaterial(Integer.parseInt(idStr.split(":")[0]));
                        data = Short.parseShort(idStr.split(":")[1]);
                    } else {
                        Material mat1 = Material.matchMaterial(idStr);
                        mat = mat1 != null ? mat1 : Material.getMaterial(Integer.parseInt(idStr));
                    }

                } catch (NumberFormatException ex) {
                    continue;
                }

                if (input.getType() == mat && input.getDurability() == data)
                    return entry.split("=")[1];

            }

        return "";

    }

    public static void convertInventoryItemNames(Inventory inv, List<String> entries) {

        for (int i = 0; i < inv.getSize(); i++)
            if (inv.getItem(i) != null)
                renameWithList(inv.getItem(i), entries);

    }

    public static void convertPlayerInventory(Player p, List<String> entries) {

        if (CraftZ.isWorld(p.getWorld()))
            convertInventoryItemNames(p.getInventory(), entries);

    }

}
