package io.github.jangobrick.craftz.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;

import io.github.jangobrick.craftz.CraftZ;

public class VehicleMoveListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onVehicleBlockCollide(VehicleMoveEvent event) {

        if (CraftZ.isWorld(event.getFrom().getWorld())) {

        }

    }

}
