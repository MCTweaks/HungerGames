package net.hetmastertje.data;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import tk.shanebee.hg.Main;
import tk.shanebee.hg.util.Util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * GameManager config <b>Client</b>
 * Version: 0.1
 */
public class GameManagerConfig {
    public static Boolean isHub;
    public static Boolean isReady;
    public static String formatUUID;
    public static String uuid;
    public static String gamemode;
    private final Main plugin;
    private File configFile;
    private FileConfiguration config;

    public GameManagerConfig(Main plugin) {
        this.plugin = plugin;
        loadConfigFile();
    }

    private void loadConfigFile() {
        // Get the server's root directory
        File serverRootDir = Bukkit.getWorldContainer();

        // Create the GameManager directory if it doesn't exist
        File gameManagerDir = new File(serverRootDir, "GameManager");
        if (!gameManagerDir.exists()) {
            Util.warning("GameManager folder not found");
            return;
        }

        // Set up the config file path
        configFile = new File(gameManagerDir, "config.yml");
        if (configFile.exists()) {
            config = YamlConfiguration.loadConfiguration(configFile);
            loadConfig();
        }

        Util.log("&7GameManager/config.yml loaded");
    }

    private void loadConfig() {
        isHub = config.getBoolean("server.isHub", false);
        isReady = config.getBoolean("server.isReady", false);
        formatUUID = config.getString("server.formatUUID", "Not set");
        uuid = config.getString("server.uuid", "Not set");
        gamemode = config.getString("server.gamemode", "Not set");
    }



    public Configuration getConfig() {
        return config;
    }

    public void save() {
        try {
            if (config != null && configFile != null) {
                config.save(configFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateConfigValue(String path, Object value) {
        if (config == null) {
            loadConfigFile(); // Ensure the config is loaded
        }

        config.set(path, value);
        save(); // Save the changes to the file
    }
}
