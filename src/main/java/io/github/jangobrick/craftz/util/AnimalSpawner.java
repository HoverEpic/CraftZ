package io.github.jangobrick.craftz.util;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

public class AnimalSpawner {

    public static void onServerTick(long tickID) {

        if (tickID % 120 == 0 && ConfigManager.getConfig("config").getBoolean("Config.mobs.animals.spawning.enable"))
            spawnRandomAnimalsInWorld(Bukkit.getWorld(ConfigManager.getConfig("config").getString("Config.world.name")));

    }

    public static void spawnRandomAnimalsInWorld(World world) {

        EntityType[] animals = {EntityType.COW, EntityType.CHICKEN, EntityType.PIG, EntityType.SHEEP};
        String[] configNames = {"cow", "chicken", "pig", "sheep"};

        for (int i = 0; i < animals.length; i++) {

            double chance = 1D - ConfigManager.getConfig("config").getDouble("Config.mobs.animals.spawning.chance." + configNames[i]);

            if (Math.random() > chance) {

                Chunk[] loadedChunks = world.getLoadedChunks();
                int randomChunkNumber = new Random().nextInt(loadedChunks.length);

                int x = new Random().nextInt(16) + 16 * loadedChunks[randomChunkNumber].getX();
                int z = new Random().nextInt(16) + 16 * loadedChunks[randomChunkNumber].getZ();
                Location loc = BlockChecker.getSafeSpawnLocationUnder(new Location(world, x, 255, z));

                if (loc == null)
                    continue;

                world.spawnEntity(loc, animals[i]);

            }

        }

    }

}
