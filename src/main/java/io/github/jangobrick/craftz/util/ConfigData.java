package io.github.jangobrick.craftz.util;

import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigData {

    public FileConfiguration config;
    public File configFile;

    public ConfigData(File configFile) {
        this.configFile = configFile;
    }

    @Override
    public boolean equals(Object anObject) {

        if (!(anObject instanceof ConfigData))
            return false;

        return configFile.equals(((ConfigData) anObject).configFile);

    }

}
