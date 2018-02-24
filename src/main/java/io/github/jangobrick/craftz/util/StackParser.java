package io.github.jangobrick.craftz.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.jangobrick.craftz.CraftZ;

public class StackParser {

    @SuppressWarnings("deprecation")
    public static ItemStack fromString(String string, boolean withAmount) {

        Material mat = Material.AIR;
        short data = 0;
        int amount = 1;

        String[] split = string.split("x", 2);
        String itemName;
        if (split.length > 1) {

            itemName = split[1];

            try {
                amount = withAmount ? Integer.parseInt(split[0]) : 1;
            } catch (NumberFormatException ex) {
                itemName = split[0] + "x" + split[1];
            }

        } else
            itemName = split[0];

        if (itemName.startsWith("'"))
            itemName = itemName.substring(1);
        if (itemName.endsWith("'"))
            itemName = itemName.substring(0, itemName.length() - 1);

        if (itemName.contains(":")) {

            try {
                Material mat1 = Material.matchMaterial(itemName.split(":")[0]);
                mat = mat1 != null ? mat1 : Material.getMaterial(Integer.parseInt(itemName.split(":")[0]));
                data = Short.parseShort(itemName.split(":")[1]);
            } catch (Exception ex) {
            }

            if (mat == null)
                CraftZ.severe("There is no item with name '" + itemName.split(":")[0] + "'! Please check the configuration files.");

        } else {

            try {
                Material mat1 = Material.matchMaterial(itemName);
                mat = mat1 != null ? mat1 : Material.getMaterial(Integer.parseInt(itemName));
            } catch (Exception ex) {
            }

            if (mat == null)
                CraftZ.severe("There is no item with name '" + itemName + "'! Please check the configuration files.");

        }

        return mat == null ? null : new ItemStack(mat, amount, data);

    }

    public static String toString(ItemStack stack, boolean withAmount) {

        if (stack == null)
            return "air";

        boolean a = withAmount && stack.getAmount() > 1;
        return (a ? stack.getAmount() + "x" : "") + (a ? "'" : "") + stack.getType().name().toLowerCase() + (a ? "'" : "")
                + (stack.getDurability() != 0 ? ":" + stack.getDurability() : "");

    }

}
