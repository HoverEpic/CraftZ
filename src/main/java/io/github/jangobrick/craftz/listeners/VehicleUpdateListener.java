package io.github.jangobrick.craftz.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleUpdateEvent;

import io.github.jangobrick.craftz.CraftZ;
import io.github.jangobrick.craftz.util.ConfigManager;

public class VehicleUpdateListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onVehicleUpdate(VehicleUpdateEvent event) {

        if (CraftZ.isWorld(event.getVehicle().getWorld()))

            if (ConfigManager.getConfig("config").getBoolean("Config.vehicles.enable")) {

            }

    }

}
