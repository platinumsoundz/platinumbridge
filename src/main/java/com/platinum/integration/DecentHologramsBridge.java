package com.platinum.integration;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class DecentHologramsBridge {

    private final JavaPlugin plugin;
    private boolean isEnabled = false;

    public DecentHologramsBridge(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Initializes the bridge and checks for DecentHolograms.
     */
    public void setup() {
        if (plugin.getServer().getPluginManager().getPlugin("DecentHolograms") != null) {
            this.isEnabled = true;
            plugin.getLogger().info("DecentHologramsBridge hooked successfully!");
        } else {
            plugin.getLogger().warning("DecentHolograms not found. DecentHologramsBridge disabled.");
        }
    }

    /**
     * Creates a new persistent hologram.
     * @param name The unique name for the hologram.
     * @param location The spawn location.
     * @param lines The initial list of lines to display.
     */
    public void createHologram(String name, Location location, List<String> lines) {
        if (!isEnabled) return;

        // Creates a persistent hologram that saves to a file
        DHAPI.createHologram(name, location, true, lines);
    }

    /**
     * Updates an existing hologram's lines.
     * @param name The name of the hologram to update.
     * @param lines The new list of lines.
     */
    public void updateHologram(String name, List<String> lines) {
        if (!isEnabled) return;

        Hologram hologram = DHAPI.getHologram(name);
        if (hologram != null) {
            DHAPI.setHologramLines(hologram, lines);
        }
    }
}