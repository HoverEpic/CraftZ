package io.github.jangobrick.craftz.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class BlockChecker {

    public static final List<Material> TRANSPARENT = Collections.unmodifiableList(Arrays.asList(
            Material.AIR,
            Material.LONG_GRASS, Material.FLOWER_POT, Material.CROPS, Material.DEAD_BUSH, Material.DOUBLE_PLANT,
            Material.WALL_SIGN, Material.SIGN_POST,
            Material.REDSTONE_WIRE, Material.REDSTONE_TORCH_ON, Material.REDSTONE_TORCH_OFF,
            Material.REDSTONE_COMPARATOR_ON, Material.REDSTONE_COMPARATOR_OFF,
            Material.DIODE_BLOCK_ON, Material.DIODE_BLOCK_OFF,
            Material.TORCH,
            Material.TRIPWIRE, Material.TRIPWIRE_HOOK
    ));

    public static Block getFirst(Material material, World world, int x, int z) {

        for (int y = 0; y < 256; y++) {
            Location tempLoc = new Location(world, x, y, z);
            if (tempLoc.getBlock().getType() == material)
                return tempLoc.getBlock();
        }

        return null;

    }

    public static Location getSafeSpawnLocationOver(Location loc) {

        for (int i = 1; i < 256 - loc.getBlockY(); i++) {

            Location tempLoc = loc.clone().add(0, i, 0);
            Material tempMat = tempLoc.getBlock().getType();
            Material tempOverMat = tempLoc.clone().add(0, 1, 0).getBlock().getType();
            Material tempUnderMat = tempLoc.clone().subtract(0, 1, 0).getBlock().getType();

            if (TRANSPARENT.contains(tempMat) && TRANSPARENT.contains(tempOverMat) && !TRANSPARENT.contains(tempUnderMat))
                return tempLoc;

        }

        return null;

    }

    public static Location getSafeSpawnLocationUnder(Location loc) {

        for (int i = 0; i < 256 - loc.getBlockY(); i++) {

            Location tempLoc = loc.clone().subtract(0, i, 0);
            Material tempMat = tempLoc.getBlock().getType();
            Material tempOverMat = tempLoc.clone().add(0, 1, 0).getBlock().getType();
            Material tempUnderMat = tempLoc.clone().subtract(0, 1, 0).getBlock().getType();

            if (TRANSPARENT.contains(tempMat) && TRANSPARENT.contains(tempOverMat) && !TRANSPARENT.contains(tempUnderMat))
                return tempLoc;

        }

        return null;

    }

    public static boolean isTree(Block block) {

        List<Block> logList = new ArrayList<Block>();
        int i = 0;

        while (true) {

            Block above = block.getRelative(0, i, 0);
            if (above == null)
                return false;;

            if (above.getType() == Material.LOG)
                logList.add(above);
            else if (above.getType() != Material.LEAVES)
                return false;
            else
                return true;

            i++;

        }

    }

}
