package io.github.jangobrick.craftz.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import io.github.jangobrick.craftz.CraftZ;

public class PlayerVisibilityBar {

    public static void updatePlayerVisibilityBar(Player p) {

        float visibility = 0.32F;

        boolean mov = CraftZ.movingPlayers.containsKey(p.getUniqueId());

        if (!mov)
            visibility -= 0.25f;

        if (p.isSneaking())
            visibility -= mov ? 0.15f : 0.3f;

        if (p.isSprinting())
            visibility = 0.6f;

        if (p.isInsideVehicle())
            visibility = mov ? 1.0f : visibility * 4;

        Material blockTypeAtPlayerLoc = p.getLocation().getBlock().getType();
        if (blockTypeAtPlayerLoc != Material.AIR)
            visibility -= 0.15f;

        if (p.isSleeping())
            visibility /= 4;

        p.setExp(visibility > 0f ? visibility : 0f);

    }

    public static float getVisibility(Player p) {
        return p.getExp();
    }

}
