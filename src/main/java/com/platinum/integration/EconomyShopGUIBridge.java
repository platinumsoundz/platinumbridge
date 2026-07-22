package com.platinum.integration;

import com.platinum.Platinum;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EconomyShopGUIBridge implements ServerBridge, Listener {
    private final Platinum plugin;
    private Economy economy = null;

    public EconomyShopGUIBridge(Platinum plugin) { this.plugin = plugin; }

    @Override
    public void init() {
        this.economy = Bukkit.getServer().getServicesManager().getRegistration(Economy.class).getProvider();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onMobKill(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer == null || economy == null) return;

        // Use the new centralized logic
        double reward = plugin.getRewardsManager().calculateReward(killer);
        economy.depositPlayer(killer, reward);
    }

    @Override public void disable() { this.economy = null; }
}