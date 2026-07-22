package com.platinum.manager;

import com.platinum.Platinum;
import com.platinum.mechanics.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public class MechanicsManager {
    private final Platinum plugin;
    private StepCounterHandler stepCounterHandler;

    public MechanicsManager(Platinum plugin) {
        this.plugin = plugin;
    }

    public void init() {
        FileConfiguration config = plugin.getConfig();

        // Movement Mechanics - All messages removed to ensure silence
        if (config.getBoolean("mechanics.double-jump.enabled", true)) {
            Bukkit.getPluginManager().registerEvents(new BackDashHandler(plugin), plugin);
        }

        if (config.getBoolean("mechanics.air-dash.enabled", true)) {
            Bukkit.getPluginManager().registerEvents(new AirDashHandler(plugin), plugin);
        }

        if (config.getBoolean("mechanics.floor-slide.enabled", true)) {
            Bukkit.getPluginManager().registerEvents(new FloorSlideHandler(plugin), plugin);
        }

        if (config.getBoolean("mechanics.grapple.enabled", true)) {
            Bukkit.getPluginManager().registerEvents(new GrappleHandler(plugin), plugin);
        }

        if (config.getBoolean("mechanics.combat-roll.enabled", true)) {
            Bukkit.getPluginManager().registerEvents(new CombatRollHandler(plugin), plugin);
        }

        // Weapon/Attack Mechanics
        if (config.getBoolean("mechanics.attack.enabled", true)) {
            Bukkit.getPluginManager().registerEvents(new AttackHandler(plugin), plugin);
        }

        // Ranged Mechanics
        if (config.getBoolean("mechanics.crossbow-gun.enabled", true)) {
            Bukkit.getPluginManager().registerEvents(new CrossbowGunHandler(plugin), plugin);
        }

        // Utility Mechanics
        if (config.getBoolean("mechanics.step-counter.enabled", true)) {
            this.stepCounterHandler = new StepCounterHandler(plugin);
            Bukkit.getPluginManager().registerEvents(this.stepCounterHandler, plugin);
        }
    }

    public StepCounterHandler getStepCounterHandler() {
        return this.stepCounterHandler;
    }

    public void shutdown() {}
}