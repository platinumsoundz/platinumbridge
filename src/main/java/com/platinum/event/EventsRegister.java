package com.platinum.events;

import com.platinum.Platinum;
import com.platinum.event.RewardListener; // Added missing import
import com.platinum.manager.MechanicsManager;
import com.platinum.menu.PlatinumMenu;
import org.bukkit.Bukkit;

public class EventsRegister {

    private final Platinum plugin;

    public EventsRegister(Platinum plugin) {
        this.plugin = plugin;
    }

    public void registerAll() {
        // Register UI components
        Bukkit.getPluginManager().registerEvents(new PlatinumMenu(), plugin);

        // Register the new RewardListener
        Bukkit.getPluginManager().registerEvents(new RewardListener(plugin), plugin);

        // Delegate game mechanics to the manager
        new MechanicsManager(plugin).init();

        plugin.getLogger().info("[Platinum 3.0] All events, listeners, and MechanicsManager registered.");
    }
}