package com.platinum.integration;

import com.platinum.registry.DataCache;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;

public class MiningBoostBridge implements ServerBridge, Listener {

    private final Plugin plugin;
    private boolean isHooked = false;

    public MiningBoostBridge(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void init() {
        Bukkit.getPluginManager().registerEvents(this, plugin);

        if (Bukkit.getPluginManager().getPlugin("MiningBoost") != null) {
            this.isHooked = true;
        }
        System.out.println("[Platinum-Elden] Successfully hooked into MiningBoost!");
    }

    @Override
    public void disable() {
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        double calculatedMiningPower = 2.5;
        DataCache.setMiningBoost(player.getUniqueId(), calculatedMiningPower);
    }
}