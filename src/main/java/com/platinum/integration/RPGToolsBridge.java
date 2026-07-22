package com.platinum.integration;

import com.platinum.registry.DataCache;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

public class RPGToolsBridge implements ServerBridge, Listener {

    private final Plugin plugin;

    public RPGToolsBridge(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void init() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        System.out.println("[Platinum-Elden] Successfully hooked into RPGTools!");
    }

    @Override
    public void disable() {}

    @EventHandler
    public void onToolUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        int combinedPower = DataCache.getCombinedBlendedPower(player);

        if (combinedPower < 10 && player.getInventory().getItemInMainHand().getType().toString().contains("NETHERITE")) {
            event.setCancelled(true);
            player.sendMessage("§b§l[Platinum] §cYour combined RPG power (" + combinedPower + "/10) is too low for Netherite!");
        }
    }
}