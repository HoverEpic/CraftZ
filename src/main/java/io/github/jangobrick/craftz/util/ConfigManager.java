package io.github.jangobrick.craftz.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {

    private static Map<String, ConfigData> configs = new HashMap<String, ConfigData>();

    public static void newConfig(String name, ConfigData data, Map<String, Object> defaults) {

        configs.put(name, data);
        FileConfiguration c = getConfig(name);

        for (String path : defaults.keySet())
            c.addDefault(path, defaults.get(path));

        getConfig(name).options().copyDefaults(true);
        saveConfig(name);

    }

    public static void newConfig(String name, JavaPlugin plugin, Map<String, Object> defaults) {
        newConfig(name, new ConfigData(new File(plugin.getDataFolder(), name + ".yml")), defaults);
    }

    public static void reloadConfig(String name) {
        configs.get(name).config = YamlConfiguration.loadConfiguration(configs.get(name).configFile);
    }

    public static void reloadConfigs() {

        for (String cfg : configs.keySet())
            reloadConfig(cfg);

    }

    public static FileConfiguration getConfig(String name) {

        if (configs.get(name).config == null)
            reloadConfig(name);

        return configs.get(name).config;

    }

    public static void saveConfig(String name) {

        if (configs.get(name).config == null || configs.get(name).configFile == null)
            return;

        try {
            getConfig(name).save(configs.get(name).configFile);
        } catch (IOException localIOException) {
        }

    }

}
