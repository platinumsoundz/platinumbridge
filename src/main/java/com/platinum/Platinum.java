package com.platinum;

import com.platinum.config.ConfigurationEngine;
import com.platinum.events.EventsRegister;
import com.platinum.manager.*;
import com.platinum.commands.BridgeReloadCommand;
import com.platinum.command.BridgeCommand; // Added import for your new class
import com.platinum.placeholder.StepPlaceholderExpansion;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Platinum extends JavaPlugin implements Listener {

    private static Platinum instance;
    private ConfigurationEngine configEngine;
    private BridgeManager bridgeManager;
    private MechanicsManager mechanicsManager;
    private RewardsManager rewardsManager;

    @Override
    public void onEnable() {
        instance = this;

        try {
            // 1. Initialize Configuration
            this.configEngine = new ConfigurationEngine(this);
            this.configEngine.loadConfigurations();

            // 2. Initialize Managers
            this.bridgeManager = new BridgeManager(this);
            this.bridgeManager.init();

            this.mechanicsManager = new MechanicsManager(this);
            this.mechanicsManager.init();

            this.rewardsManager = new RewardsManager(this);

            // 3. Register Events and Commands
            new EventsRegister(this).registerAll();
            getCommand("bridgereload").setExecutor(new BridgeReloadCommand(this));

            // Registered the new bridge command here
            getCommand("open_sellgui").setExecutor(new BridgeCommand());

            // 4. Register Placeholders if PAPI exists
            if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
                new StepPlaceholderExpansion(this, this.mechanicsManager.getStepCounterHandler()).register();
            }

            getLogger().info("Platinum 3.0 system successfully loaded.");

        } catch (Exception e) {
            getLogger().severe("CRITICAL ERROR: Platinum 3.0 failed to initialize!");
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        if (bridgeManager != null) bridgeManager.shutdown();
        if (mechanicsManager != null) mechanicsManager.shutdown();
        if (configEngine != null) configEngine.saveConfigurations();
        instance = null;
    }

    public static Platinum getInstance() { return instance; }
    public BridgeManager getBridgeManager() { return this.bridgeManager; }
    public MechanicsManager getMechanicsManager() { return this.mechanicsManager; }
    public RewardsManager getRewardsManager() { return this.rewardsManager; }
}