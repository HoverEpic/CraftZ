package io.github.jangobrick.craftz.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.github.jangobrick.craftz.CraftZ;

public class ZombieSpawner implements Listener {

    private static int ticksForAutoSpawn = 0;
    private static Map<String, Integer> cooldowns = new HashMap<String, Integer>();

    public static void addSpawns() {

        if (WorldData.get().getConfigurationSection("Data.zombiespawns") != null)

            for (String spawnEntry : WorldData.get().getConfigurationSection("Data.zombiespawns").getKeys(false))
                addSpawn(spawnEntry);

    }

    public static void addSpawn(String spawn) {
        cooldowns.put(spawn, 0);
    }

    public static Zombie evalZombieSpawn(ConfigurationSection spnptSec) {

        int spnLocX = spnptSec.getInt("coords.x");
        int spnLocY = spnptSec.getInt("coords.y");
        int spnLocZ = spnptSec.getInt("coords.z");
        Location spnLoc = new Location(CraftZ.world(), spnLocX, spnLocY, spnLocZ);

        Location locToSpawn = BlockChecker.getSafeSpawnLocationOver(spnLoc);
        if (locToSpawn == null)
            locToSpawn = BlockChecker.getSafeSpawnLocationUnder(spnLoc);

        int maxZombiesInRadius = spnptSec.getInt("max-zombies-in-radius");
        int maxZombiesRadius = spnptSec.getInt("max-zombies-radius");

        if (locToSpawn != null && !EntityChecker.areEntitiesNearby(locToSpawn, maxZombiesRadius, EntityType.ZOMBIE, maxZombiesInRadius)) {

            int zombies = 0;
            int maxZombies = ConfigManager.getConfig("config").getInt("Config.mobs.zombies.spawning.maxzombies");

            for (Entity ent : CraftZ.world().getEntities())
                if (ent.getType() == EntityType.ZOMBIE)
                    zombies++;

            if (zombies <= maxZombies) {

                Entity ent = CraftZ.world().spawnEntity(locToSpawn, EntityType.ZOMBIE);
                return (Zombie) ent;

            } else
                return null;

        } else
            return null;

    }

    public static void onServerTick() {

        for (String str : cooldowns.keySet()) {

            cooldowns.put(str, cooldowns.get(str) + 1);

            if (cooldowns.get(str) >= ConfigManager.getConfig("config").getInt("Config.mobs.zombies.spawning.interval") * 20) {

                cooldowns.put(str, 0);

                Set<String> spts_zombies_set = WorldData.get().getConfigurationSection("Data.zombiespawns").getKeys(false);
                if (spts_zombies_set != null && !spts_zombies_set.isEmpty()) {

                    ConfigurationSection configSec = WorldData.get().getConfigurationSection("Data.zombiespawns." + str);
                    if (configSec == null)
                        return;

                    Zombie spawnedZombie = evalZombieSpawn(configSec);
                    if (spawnedZombie == null)
                        return;

                    equipZombie(spawnedZombie);

                }

            }

        }

        if (ConfigManager.getConfig("config").getBoolean("Config.mobs.zombies.spawning.enable-auto-spawn")) {

            ticksForAutoSpawn++;
            if (PlayerManager.getPlayCount() > 0 && ticksForAutoSpawn >= ConfigManager.getConfig("config")
                    .getInt("Config.mobs.zombies.spawning.auto-spawning-interval") * 20 / PlayerManager.getPlayCount()) {

                ticksForAutoSpawn = 0;

                Player p = PlayerManager.randomPlayer();
                if (p == null)
                    return;

                Location randLoc = p.getLocation().add(new Random().nextInt(128) - 64, 0,
                        new Random().nextInt(128) - 64);

                Location locToSpawn = BlockChecker.getSafeSpawnLocationOver(randLoc);
                if (locToSpawn == null)
                    locToSpawn = BlockChecker.getSafeSpawnLocationUnder(randLoc);

                if (locToSpawn != null) {

                    int zombies = 0;
                    int maxZombies = ConfigManager.getConfig("config").getInt("Config.mobs.zombies.spawning.maxzombies");

                    for (Entity ent : p.getWorld().getEntities())
                        if (ent.getType() == EntityType.ZOMBIE)
                            zombies++;

                    if (zombies <= maxZombies) {
                        Entity ent = p.getWorld().spawnEntity(locToSpawn, EntityType.ZOMBIE);
                        equipZombie((Zombie) ent);
                    }

                }

            }

        }

    }

    public static void equipZombie(Zombie zombie) {

        if (zombie.isBaby() || !zombie.getActivePotionEffects().isEmpty() // already equipped
                || zombie.getHealth() != zombie.getMaxHealth())
            return;

        int speed = ConfigManager.getConfig("config").getInt("Config.mobs.zombies.properties.speed-boost");
        int damage = ConfigManager.getConfig("config").getInt("Config.mobs.zombies.properties.damage-boost");

        if (speed > 0)
            zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, speed - 1));
        if (damage > 0)
            zombie.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, damage - 1));

        if (new Random().nextInt(7) < 1 && ConfigManager.getConfig("config").getBoolean("Config.mobs.zombies.spawning.enable-mini-zombies")) {
            zombie.setBaby(true);
            zombie.removePotionEffect(PotionEffectType.SPEED);
            zombie.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 0));
        }

        int health = ConfigManager.getConfig("config").getInt("Config.mobs.zombies.properties.health");
        if (health > 0)
            zombie.setHealth(health);

    }

}
