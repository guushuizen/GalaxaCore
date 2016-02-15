package me.iamguus.galaxacore.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

/**
 * Created by Guus on 29-1-2016.
 */
public class ConfigUtil {

    private static ConfigUtil instance;

    private File pluginDir;

    private File configFile;
    private FileConfiguration config;

    public void setup(Plugin p) {
        this.pluginDir = p.getDataFolder();

        this.configFile = new File(this.pluginDir, "config.yml");
        if (!configFile.exists()) {
            p.saveResource("config.yml", true);
        }
        this.config = YamlConfiguration.loadConfiguration(this.configFile);
    }

    public File getConfigFile() {
        return configFile;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void saveConfig() {
        try {
            this.config.save(this.configFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static ConfigUtil get() {
        if (instance == null) {
            instance = new ConfigUtil();
        }

        return instance;
    }
}
