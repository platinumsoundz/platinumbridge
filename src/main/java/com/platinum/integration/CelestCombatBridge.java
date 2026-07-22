package com.platinum.integration;

import com.platinum.registry.DataCache;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class CelestCombatBridge implements ServerBridge, Listener {

    private final Plugin plugin;
    private boolean isHooked = false;

    public CelestCombatBridge(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void init() {
        Bukkit.getPluginManager().registerEvents(this, plugin);

        if (Bukkit.getPluginManager().getPlugin("CelestCombatPro") != null) {
            this.isHooked = true;
            plugin.getLogger().info("[Platinum] Successfully verified CelestCombatPro tracking matrix!");
        }
        System.out.println("[Platinum-Elden] Successfully hooked into CelestCombat-Pro!");
    }

    @Override
    public void disable() {
        // Clean up any damage listeners or combat tag handlers if necessary
    }

    public void updateCombatStatus(Player player, boolean inCombat) {
        if (!isHooked || player == null) return;
        DataCache.setCombatState(player.getUniqueId(), inCombat);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        DataCache.setCombatState(event.getPlayer().getUniqueId(), false);
    }
}