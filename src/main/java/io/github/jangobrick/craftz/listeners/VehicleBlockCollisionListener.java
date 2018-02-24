package io.github.jangobrick.craftz.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.vehicle.VehicleBlockCollisionEvent;

import io.github.jangobrick.craftz.CraftZ;
import io.github.jangobrick.craftz.util.ConfigManager;

public class VehicleBlockCollisionListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onVehicleBlockCollide(VehicleBlockCollisionEvent event) {

        if (CraftZ.isWorld(event.getVehicle().getWorld()))

            if (ConfigManager.getConfig("config").getBoolean("Config.vehicles.enable")) {

                Vehicle vehicle = event.getVehicle();
                Entity passenger = vehicle.getPassenger();
                if (!(passenger instanceof Player))
                    return;

                Block block = event.getBlock();

                if (event.getVehicle() instanceof Minecart) {

                    Minecart cart = (Minecart) vehicle;

                    if (block.getRelative(BlockFace.UP).getType() == Material.AIR && block.getRelative(0, 2, 0).getType() == Material.AIR) {
                        Location newLoc = block.getRelative(BlockFace.UP).getLocation();
                        cart.teleport(newLoc, TeleportCause.PLUGIN);
                    } else
                        cart.setDamage(cart.getDamage() + 1);

                }

            }

    }

}
