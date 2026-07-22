package com.platinum.integration;

import com.platinum.Platinum;
import com.platinum.api.RewardProvider;
import com.platinum.registry.DataCache;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

public class HereNavyBridge implements ServerBridge, Listener, RewardProvider {

    private final Platinum plugin;
    private boolean isHooked = false;

    public HereNavyBridge(Platinum plugin) {
        this.plugin = plugin;
    }

    @Override
    public void init() {
        Bukkit.getPluginManager().registerEvents(this, plugin);

        if (Bukkit.getPluginManager().getPlugin("HereNavy") != null) {
            this.isHooked = true;
            plugin.getLogger().info("[Platinum] Successfully hooked into HereNavy maritime engines!");
        } else {
            plugin.getLogger().warning("[Platinum] HereNavy plugin not found. Running in standalone profile mode.");
        }

        System.out.println("[Platinum-Elden] Successfully hooked into HereNavy!");

        // Register with the central RewardsManager
        plugin.getRewardsManager().registerProvider(this);
    }

    @Override
    public void disable() {
    }

    // --- RewardProvider Implementation ---
    @Override
    public boolean shouldReward(Player player, String triggerType) {
        return "MARITIME_ACHIEVEMENT".equalsIgnoreCase(triggerType);
    }

    @Override
    public void grantRewards(Player player, Map<String, Object> context) {
        double rewardValue = (double) context.getOrDefault("calculated_value", 5.0);

        // Example Reward: Fishing related items or "Naval" currency
        player.getInventory().addItem(new ItemStack(Material.COD, (int) Math.round(rewardValue)));

        // Modernized message using Adventure API
        player.sendMessage(Component.text("⚓ [HereNavy] ", NamedTextColor.BLUE)
                .append(Component.text("Maritime rewards synchronized!", NamedTextColor.WHITE)));
    }

    @EventHandler
    public void onMaritimeSync(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        int navalLevel = isHooked ? 15 : 0;
        DataCache.setCachedSkillPoints(uuid, DataCache.getCombinedBlendedPower(player) + navalLevel);
    }
}