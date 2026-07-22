package com.platinum.integration;

import com.platinum.registry.DataCache;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;

public class LevelledMobsBridge implements ServerBridge, Listener {

    private final Plugin plugin;
    private boolean isHooked = false;

    public LevelledMobsBridge(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void init() {
        Bukkit.getPluginManager().registerEvents(this, plugin);

        if (Bukkit.getPluginManager().getPlugin("LevelledMobs") != null) {
            this.isHooked = true;
            plugin.getLogger().info("[Platinum] Hooked into LevelledMobs level calculation scaling!");
        }
        System.out.println("[Platinum-Elden] Successfully hooked into LevelledMobs!");
    }

    @Override
    public void disable() {
    }

    @EventHandler
    public void onCombatScale(EntityDamageByEntityEvent event) {
        if (!isHooked || !(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();
        double eldenMultiplier = DataCache.getEldenMultiplier(player.getUniqueId());
        if (eldenMultiplier > 1.0) {
            event.setDamage(event.getDamage() * eldenMultiplier);
        }
    }
}