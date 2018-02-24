package io.github.jangobrick.craftz.util;

import static io.github.jangobrick.craftz.CraftZ.info;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.io.Files;

import io.github.jangobrick.craftz.CraftZ;

public class WorldData {

    private static Map<String, ConfigData> configs = new HashMap<String, ConfigData>();

    public static void setup() {

        tryUpdate();

        File worldsFolder = new File(CraftZ.i.getDataFolder(), "worlds/");
        if (!worldsFolder.exists())
            return;

        for (File file : worldsFolder.listFiles())
            if (file.getName().toLowerCase().endsWith(".yml"))
                reload(file.getName().substring(0, file.getName().length() - 4));

    }

    private static void tryUpdate() {

        File old = new File(CraftZ.i.getDataFolder(), "data.yml");
        if (old.exists())

            try {

                File newFile = new File(CraftZ.i.getDataFolder(), "worlds/" + CraftZ.worldName() + ".yml");
                if (newFile.exists())
                    return;

                newFile.getParentFile().mkdirs();
                newFile.createNewFile();
                Files.copy(old, newFile);
                old.delete();

            } catch (IOException ex) {
                ex.printStackTrace();
            }

    }

    private static void tryUpdateConfig(String world) {

        int version = get(world).getInt("Data.never-ever-modify.configversion");

        if (version < 1) {
            uc_1(world);
            get(world).set("Data.never-ever-modify.configversion", 1);
            save(world);
        }

    }

    @SuppressWarnings("deprecation")
    private static void uc_1(String world) {

        info("Converting world data for '" + world + "' to version 1");

        ConfigurationSection plSec = get(world).getConfigurationSection("Data.players");
        if (plSec == null)
            info(" -  World data for '" + world + "' seems to be empty, no conversion needed");

        for (String key : plSec.getKeys(false)) {

            UUID id = Bukkit.getOfflinePlayer(key).getUniqueId();
            if (id == null) {
                info(" -  Not able to convert player '" + key + "', he will be deleted");
                plSec.set(key, null);
                continue;
            }

            int thirst = get(world).getInt("Data.players." + key + ".thirst");
            int zombies = get(world).getInt("Data.players." + key + ".zombiesKilled");
            int players = get(world).getInt("Data.players." + key + ".playersKilled");
            int mins = get(world).getInt("Data.players." + key + ".minsSurvived");
            boolean bleeding = get(world).getBoolean("Data.players." + key + ".bleeding");
            boolean bonesBroken = get(world).getBoolean("Data.players." + key + ".bonesBroken");
            boolean poisoned = get(world).getBoolean("Data.players." + key + ".poisoned");

            String conv = thirst + "|" + zombies + "|" + players + "|" + mins + "|" + (bleeding ? "1" : "0")
                    + "|" + (bonesBroken ? "1" : "0") + "|" + (poisoned ? "1" : "0");
            plSec.set(id.toString(), conv);
            plSec.set(key, null);

        }

    }

    private static void load(String world) {

        get(world).options().header("Data for the CraftZ plugin by JangoBrick"
                + "\nThis is for the world \"" + world + "\"");

        get(world).options().copyDefaults(true);
        save(world);

        tryUpdateConfig(world);

    }

    public static void reload(String world) {

        ConfigData data = new ConfigData(new File(CraftZ.i.getDataFolder(), "worlds/" + world + ".yml"));
        data.config = YamlConfiguration.loadConfiguration(data.configFile);

        if (!configs.containsKey(world))
            configs.put(world, data);

        load(world);

    }

    public static void reload() {
        reload(CraftZ.worldName());
    }

    public static FileConfiguration get(String world) {

        if (!configs.containsKey(world))
            reload(world);

        return configs.get(world).config;

    }

    public static FileConfiguration get() {
        return get(CraftZ.worldName());
    }

    public static void save(String world) {

        if (configs.get(world).config == null || configs.get(world).configFile == null)
            return;

        try {
            get(world).save(configs.get(world).configFile);
        } catch (IOException ex) {
            CraftZ.i.getLogger().log(Level.SEVERE, "Could not save config to "
                    + configs.get(world).configFile, ex);
        }

    }

    public static void save() {
        save(CraftZ.worldName());
    }

}
