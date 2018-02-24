package io.github.jangobrick.craftz.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

import io.github.jangobrick.craftz.CraftZ;

public class DeadPlayer {

    public static List<Material> weapons = Arrays.asList(new Material[]{
        Material.WOOD_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.GOLD_SWORD, Material.DIAMOND_SWORD,
        Material.WOOD_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.GOLD_AXE, Material.DIAMOND_AXE,
        Material.BOW
    });

    public String p;
    public ArrayList<ItemStack> inventory = new ArrayList<ItemStack>();
    public ItemStack[] armor = new ItemStack[4];
    public UUID uuid;

    private DeadPlayer(Player p) {

        this.p = p.getName();

        ItemStack[] contents = p.getInventory().getContents();
        for (ItemStack st : contents)
            if (st != null)
                inventory.add(st);
        armor = p.getInventory().getArmorContents();

        Zombie zombie = (Zombie) p.getLocation().getWorld().spawnEntity(p.getLocation(), EntityType.ZOMBIE);
        zombie.setBaby(false);
        zombie.setVillager(true);
        zombie.setCanPickupItems(false);
        zombie.setRemoveWhenFarAway(false);
        zombie.setCustomName(this.p);
        zombie.setCustomNameVisible(true);
        zombie.getEquipment().setArmorContents(armor);
        zombie.getEquipment().setItemInHand(getWeaponItem());
        zombie.getEquipment().setItemInHandDropChance(0);

        ZombieSpawner.equipZombie(zombie);

        uuid = zombie.getUniqueId();

    }

    private DeadPlayer(String s) {

        String[] spl = s.split("\\|");
        p = spl[0];

        String[] uuidParts = spl[1].split(",");
        uuid = new UUID(Long.parseLong(uuidParts[0]), Long.parseLong(uuidParts[1]));

        String[] armorStrings = spl[2].split(",");
        for (int i = 0; i < armorStrings.length; i++)
            armor[i] = StackParser.fromString(armorStrings[i], true);

        String[] invStrings = spl[3].split(",");
        for (int i = 0; i < invStrings.length; i++)
            inventory.add(StackParser.fromString(invStrings[i], true));

    }

    public ItemStack getWeaponItem() {

        for (ItemStack stack : inventory)
            if (weapons.contains(stack.getType()))
                return stack;

        return null;

    }

    public void remove() {
        CraftZ.deadPlayers.remove(this);
    }

    @Override
    public String toString() {

        String s = p + "|" + uuid.getMostSignificantBits() + "," + uuid.getLeastSignificantBits() + "|";

        s += StackParser.toString(armor[0], true) + "," + StackParser.toString(armor[1], true)
                + "," + StackParser.toString(armor[2], true) + "," + StackParser.toString(armor[3], true) + "|";

        for (ItemStack stack : inventory)
            s += StackParser.toString(stack, true) + ",";

        if (s.endsWith(","))
            s = s.substring(0, s.length() - 1);

        if (s.endsWith("|"))
            s += "0";

        return s;

    }

    public static void create(Player p) {
        CraftZ.deadPlayers.add(new DeadPlayer(p));
        saveDeadPlayers();
    }

    public static void saveDeadPlayers() {

        ArrayList<String> strings = new ArrayList<String>();

        for (DeadPlayer dp : CraftZ.deadPlayers)
            strings.add(dp.toString());

        WorldData.get().set("Data.dead", strings);
        WorldData.save();

    }

    public static void loadDeadPlayers() {

        CraftZ.deadPlayers.clear();
        List<String> strings = WorldData.get().getStringList("Data.dead");

        for (String s : strings)
            CraftZ.deadPlayers.add(new DeadPlayer(s));

    }

    public static DeadPlayer getDeadPlayer(UUID uuid) {

        for (DeadPlayer dp : CraftZ.deadPlayers)
            if (dp.uuid.equals(uuid))
                return dp;

        return null;

    }

}
