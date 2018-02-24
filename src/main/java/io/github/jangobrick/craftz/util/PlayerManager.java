package io.github.jangobrick.craftz.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.github.jangobrick.craftz.CraftZ;

public class PlayerManager {

    private static HashMap<UUID, AdditionalCraftZData> players = new HashMap<UUID, AdditionalCraftZData>();

    public static Player p(UUID uuid) {
        return Bukkit.getPlayer(uuid);
    }

    public static ConfigurationSection getConfig() {
        return WorldData.get();
    }

    public static void savePlayerToConfig(Player p) {

        if (players.containsKey(p.getUniqueId())) {

            AdditionalCraftZData d = getData(p);
            getConfig().set("Data.players." + p.getUniqueId(), d.thirst + "|" + d.zombiesKilled + "|" + d.playersKilled + "|" + d.minutesSurvived
                    + "|" + (d.bleeding ? "1" : "0") + "|" + (d.poisoned ? "1" : "0") + "|" + (d.bonesBroken ? "1" : "0"));

            WorldData.save();

        }

    }

    public static void loadPlayer(Player p, boolean forceRespawn) {

        if (players.containsKey(p.getUniqueId()) && !forceRespawn)
            return;

        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 30, 1000));

        if (wasInWorld(p) && !forceRespawn) {

            putPlayer(p, false);
            p.setLevel(players.get(p.getUniqueId()).thirst);

        } else {

            if (ConfigManager.getConfig("config").getBoolean("Config.players.clear-inventory-on-spawn")) {
                p.getInventory().clear();
                p.getInventory().setArmorContents(new ItemStack[4]);
            }

            p.setHealth(20);
            p.setFoodLevel(20);
            spawnPlayerAtRandomSpawn(p);

            putPlayer(p, true);

            savePlayerToConfig(p);

            p.setLevel(players.get(p.getUniqueId()).thirst);

        }

        ScoreboardHelper.createPlayer(p);

    }

    private static void putPlayer(Player p, boolean defaults) {

        if (defaults)
            players.put(p.getUniqueId(), new AdditionalCraftZData(20, 0, 0, 0, false, false, false));
        else {

            String confData = getConfig().getString("Data.players." + p.getUniqueId());
            String[] spl = confData.split("\\|");

            int thirst = spl.length > 0 ? Integer.valueOf(spl[0]) : 20;
            int zombies = spl.length > 1 ? Integer.valueOf(spl[1]) : 0;
            int playersk = spl.length > 2 ? Integer.valueOf(spl[2]) : 0;
            int minutes = spl.length > 3 ? Integer.valueOf(spl[3]) : 0;
            boolean bleeding = spl.length > 4 ? spl[4].equals("1") : false;
            boolean bonesBroken = spl.length > 5 ? spl[5].equals("1") : false;
            boolean poisoned = spl.length > 6 ? spl[6].equals("1") : false;

            players.put(p.getUniqueId(), new AdditionalCraftZData(thirst, zombies, playersk, minutes, bleeding, bonesBroken, poisoned));

        }

    }

    public static void spawnPlayerAtRandomSpawn(Player p) {

        if (!getConfig().contains("Data.playerspawns"))
            return;

        Set<String> spts_players_set = getConfig().getConfigurationSection("Data.playerspawns").getKeys(false);

        if (spts_players_set != null && !spts_players_set.isEmpty()) {

            Object[] spts_players = spts_players_set.toArray();
            int taken = new Random().nextInt(spts_players.length);

            ConfigurationSection configSec = getConfig().getConfigurationSection("Data.playerspawns." + spts_players[taken]);
            if (configSec == null)
                return;

            int spnLocX = configSec.getInt("coords.x");
            int spnLocY = configSec.getInt("coords.y");
            int spnLocZ = configSec.getInt("coords.z");
            World spnWorld = CraftZ.world();
            Location spnLoc = new Location(spnWorld, spnLocX, spnLocY, spnLocZ);

            p.teleport(BlockChecker.getSafeSpawnLocationOver(spnLoc));

            p.sendMessage(ChatColor.YELLOW + CraftZ.getMsg("Messages.spawned").replaceAll("%s", configSec.getString("name")));

        }

    }

    public static void saveAllPlayersToConfig() {

        for (Player p : CraftZ.world().getPlayers())
            savePlayerToConfig(p);

    }

    public static void resetPlayer(Player p) {

        getConfig().set("Data.players." + p.getUniqueId(), null);
        WorldData.save();

        ScoreboardHelper.removePlayer(p.getUniqueId());
        players.remove(p.getUniqueId());

    }

    public static AdditionalCraftZData getData(UUID p) {
        if (!players.containsKey(p))
            loadPlayer(p(p), false);
        return players.get(p);
    }

    public static AdditionalCraftZData getData(Player p) {
        if (!players.containsKey(p.getUniqueId()))
            loadPlayer(p, false);
        return players.get(p.getUniqueId());
    }

    public static void onServerTick(long tickID) {

        List<UUID> toRemove = new ArrayList<UUID>();

        for (UUID id : players.keySet()) {

            if (isNotPlaying(id)) {
                toRemove.add(id);
                continue;
            }

            Player p = p(id);
            AdditionalCraftZData data = players.get(id);

            PlayerVisibilityBar.updatePlayerVisibilityBar(p);

            if (ConfigManager.getConfig("config").getBoolean("Config.players.medical.thirst.enable")) {

                Biome biome = p.getLocation().getBlock().getBiome();
                boolean desert = biome == Biome.DESERT || biome == Biome.DESERT_HILLS || biome == Biome.BIRCH_FOREST_HILLS;
                int ticksNeeded = desert ? ConfigManager.getConfig("config").getInt("Config.players.medical.thirst.ticks-desert")
                        : ConfigManager.getConfig("config").getInt("Config.players.medical.thirst.ticks-normal");

                if (tickID % ticksNeeded == 0) {

                    if (data.thirst > 0) {
                        data.thirst--;
                        p.setLevel(data.thirst);
                    } else
                        p.damage(2);

                    if (ConfigManager.getConfig("config").getBoolean("Config.players.medical.thirst.show-messages"))

                        if (data.thirst <= 8 && data.thirst > 1 && data.thirst % 2 == 0)
                            p.sendMessage(ChatColor.RED + CraftZ.getMsg("Messages.thirsty"));
                        else if (data.thirst <= 1)
                            p.sendMessage(ChatColor.DARK_RED + CraftZ.getMsg("Messages.thirsty-dehydrating"));

                }

            }

            if (tickID % 1200 == 0)
                data.minutesSurvived++;

            if (tickID % 10 == 0) {
                ItemRenamer.convertPlayerInventory(p, ConfigManager.getConfig("config").getStringList("Config.change-item-names.names"));
                ScoreboardHelper.update();
            }

            if (tickID % 30 == 0)

                if (ConfigManager.getConfig("config").getBoolean("Config.world.world-border.enable")) {

                    double dmg = getWorldBorderDamage(p, ConfigManager.getConfig("config").getDouble("Config.world.world-border.radius"), getLobby());

                    if (dmg > 0) {
                        if (tickID % 200 == 0)
                            p.sendMessage(CraftZ.getPrefix() + " " + CraftZ.getMsg("Messages.out-of-world"));
                        p.damage(dmg);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 20, 1));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 1));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 50, 1));
                    }

                }

            if (tickID % 200 == 0) {

                if (data.bleeding)
                    p.damage(1);

                if (data.poisoned) {
                    p.damage(1);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 20, 1));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10, 1));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 30, 1));
                }

            }

            if (data.bonesBroken)
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 2), true);

        }

        for (UUID id : toRemove) {

            Player p = p(id);
            if (p != null)
                savePlayerToConfig(p);

            ScoreboardHelper.removePlayer(id);

            players.remove(id);

        }

    }

    public static double getWorldBorderDamage(Player p, double radius, Location spawn) {

        //TODO: make compatible with lobby in different world.
        //Perhaps add configurable world center?
        Location ploc = p.getLocation();
        Location loc = new Location(ploc.getWorld(), spawn.getX(), ploc.getY(), spawn.getZ());

        double dist = ploc.distance(loc) - radius;
        if (dist <= 0)
            return 0;
        else
            return dist / 60D;

    }

    public static boolean isInsideOfLobby(Player p) {

        Location lobby = getLobby();
        int radius = ConfigManager.getConfig("config").getInt("Config.world.lobby.radius");

        return p.getWorld().getName().equals(lobby.getWorld().getName()) && lobby.distance(p.getLocation()) <= radius;

    }

    public static Location getLobby() {

        Location lobby = CraftZ.world().getSpawnLocation();
        String w = ConfigManager.getConfig("config").getString("Config.world.lobby.world");
        if (w != null)
            lobby.setWorld(Bukkit.getWorld(w));
        lobby.setX(ConfigManager.getConfig("config").getDouble("Config.world.lobby.x"));
        lobby.setY(ConfigManager.getConfig("config").getDouble("Config.world.lobby.y"));
        lobby.setZ(ConfigManager.getConfig("config").getDouble("Config.world.lobby.z"));

        return lobby;

    }

    public static boolean wasInWorld(Player p) {
        return getConfig().contains("Data.players." + p.getUniqueId());
    }

    public static boolean isInWorld(Player p) {
        return players.containsKey(p.getUniqueId());
    }

    public static int getPlayCount() {
        return players.size();
    }

    public static Player randomPlayer() {

        List<Player> players = CraftZ.world().getPlayers();
        if (players.isEmpty())
            return null;
        Collections.shuffle(players);

        Player chosen = players.get(0);

        if (!isInsideOfLobby(chosen))
            return chosen;

        boolean otherExists = false;
        for (Player p : players)

            if (!isInsideOfLobby(p)) {
                otherExists = true;
                break;
            }

        if (otherExists)
            return randomPlayer();

        return null;

    }

    public static boolean isNotPlaying(UUID id) {
        Player p = p(id);
        return p == null || !CraftZ.isWorld(p.getWorld()) || isInsideOfLobby(p) || !players.containsKey(id);
    }

}
