package com.platinum.integration;

import com.platinum.Platinum;
import com.platinum.api.RewardProvider;
import com.platinum.registry.DataCache;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class SimpleSurvivalRPGBridge implements ServerBridge, Listener, RewardProvider {

    private final Platinum plugin;
    private boolean isHooked = false;

    public SimpleSurvivalRPGBridge(Platinum plugin) {
        this.plugin = plugin;
    }

    @Override
    public void init() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        if (Bukkit.getPluginManager().getPlugin("SimpleSurvivalRPG") != null) {
            this.isHooked = true;
        }
        System.out.println("[Platinum-Elden] Successfully hooked into SimpleSurvivalRPG!");

        plugin.getRewardsManager().registerProvider(this);
    }

    @Override
    public void disable() {}

    @Override
    public boolean shouldReward(Player player, String triggerType) {
        return "SURVIVAL_MILESTONE".equalsIgnoreCase(triggerType);
    }

    @Override
    public void grantRewards(Player player, Map<String, Object> context) {
        double rewardValue = (double) context.getOrDefault("calculated_value", 5.0);

        player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, (int) Math.round(rewardValue)));

        // Modernized message using Adventure API
        player.sendMessage(Component.text("❤ [SimpleSurvivalRPG] ", NamedTextColor.GREEN)
                .append(Component.text("Survival rewards synchronized!", NamedTextColor.WHITE)));
    }

    @EventHandler
    public void syncSurvivalAttributes(PlayerJoinEvent event) {
        if (!isHooked) return;
        Player player = event.getPlayer();
        double multiplier = DataCache.getEldenMultiplier(player.getUniqueId());

        // Modernized Attribute access
        if (player.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null) {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0 * multiplier);
        }
    }
}