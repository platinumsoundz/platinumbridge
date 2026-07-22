package com.platinum.integration;

import com.platinum.registry.DataCache;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;

public class EliteMobsBridge implements ServerBridge, Listener {

    private final Plugin plugin;
    private boolean isHooked = false;

    public EliteMobsBridge(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void init() {
        Bukkit.getPluginManager().registerEvents(this, plugin);

        if (Bukkit.getPluginManager().getPlugin("EliteMobs") != null) {
            this.isHooked = true;
            plugin.getLogger().info("[Platinum] Successfully verified EliteMobs API hook status!");
        } else {
            plugin.getLogger().warning("[Platinum] EliteMobs plugin not found. Core GUI integrations disabled.");
        }

        plugin.getLogger().info("[Elden] Successfully hooked into EliteMobs!");
    }

    @Override
    public void disable() {
    }

    @EventHandler
    public void onEliteUpgradeClick(InventoryClickEvent event) {
        if (!isHooked || event.getClickedInventory() == null) return;

        String title = event.getView().getTitle();
        if (title.contains("Custom Upgrade") || title.contains("Elite Shop") || title.contains("Upgrade")) {

            if (event.getWhoClicked() instanceof Player) {
                Player player = (Player) event.getWhoClicked();
                int blendedPower = DataCache.getCombinedBlendedPower(player);

                if (blendedPower < 50) {
                    event.setCancelled(true);
                    player.closeInventory();
                    player.sendMessage("§b§l[Platinum] §cYour Combined Power (" + blendedPower + "/50) is too low to perform Elite Upgrades!");
                }
            }
        }
    }
}