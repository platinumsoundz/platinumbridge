package com.platinum.config;

import org.bukkit.plugin.java.JavaPlugin;

public class ConfigurationEngine {
    private final JavaPlugin plugin;

    public ConfigurationEngine(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadConfigurations() {
        // Automatically creates and updates config.yml if needed
        plugin.saveDefaultConfig();
    }

    public void saveConfigurations() {
        plugin.saveConfig();
    }
}