package com.platinum.integration;

import com.platinum.Platinum;
import com.platinum.api.RewardProvider;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import java.util.Map;
import org.bukkit.entity.Player;

public class VaultBridge implements ServerBridge, RewardProvider {

    private static Economy economy = null;
    private final Platinum plugin;

    public VaultBridge(Platinum plugin) {
        this.plugin = plugin;
    }

    @Override
    public void init() {
        if (setupEconomy()) {
            plugin.getLogger().info("[Platinum] Successfully hooked into Vault Economy!");
            plugin.getRewardsManager().registerProvider(this);
        } else {
            plugin.getLogger().warning("[Platinum] Vault or Economy plugin not found!");
        }
    }

    // Required by ServerBridge interface[cite: 10]
    @Override
    public void disable() {
        economy = null;
    }

    public boolean setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    public static Economy getEconomy() {
        return economy;
    }

    @Override
    public boolean shouldReward(Player player, String triggerType) {
        return true;
    }

    @Override
    public void grantRewards(Player player, Map<String, Object> context) {
        if (economy == null) return;

        double rewardValue = (double) context.getOrDefault("calculated_value", 10.0);

        economy.depositPlayer(player, rewardValue);

        player.sendMessage(Component.text("⛁ [Economy] ", NamedTextColor.YELLOW)
                .append(Component.text("Added " + rewardValue + " to your balance!", NamedTextColor.WHITE)));
    }
}