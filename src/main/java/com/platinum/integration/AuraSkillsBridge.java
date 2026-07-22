package com.platinum.integration;

import com.platinum.Platinum;
import com.platinum.api.RewardProvider;
import com.platinum.registry.DataCache;
import dev.aurelium.auraskills.api.AuraSkillsApi;
import dev.aurelium.auraskills.api.event.skill.SkillLevelUpEvent;
import dev.aurelium.auraskills.api.registry.NamespacedId;
import dev.aurelium.auraskills.api.stat.Stat;
import dev.aurelium.auraskills.api.user.SkillsUser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.UUID;

public class AuraSkillsBridge implements ServerBridge, Listener, RewardProvider {

    private final Platinum plugin;
    private AuraSkillsApi auraSkillsApi;

    private boolean isHooked = false;
    private boolean celestCombatHooked = false;
    private Plugin celestCombatPlugin;

    // Flag to prevent console spam
    private boolean errorLogged = false;

    public AuraSkillsBridge(Platinum plugin) {
        this.plugin = plugin;
    }

    @Override
    public void init() {
        if (Bukkit.getPluginManager().getPlugin("AuraSkills") == null) {
            plugin.getLogger().warning("[Platinum] AuraSkills not found! Integration features disabled.");
        } else {
            try {
                this.auraSkillsApi = AuraSkillsApi.get();
                this.isHooked = true;
                plugin.getLogger().info("[Platinum] Successfully hooked into AuraSkills API!");
                registerCustomStats();
            } catch (Exception e) {
                plugin.getLogger().severe("[Platinum] Failed to initialize AuraSkills Bridge: " + e.getMessage());
                this.isHooked = false;
            }
        }

        this.celestCombatPlugin = Bukkit.getPluginManager().getPlugin("CelestCombatPro");
        if (celestCombatPlugin != null) {
            this.celestCombatHooked = true;
            plugin.getLogger().info("[Platinum] Successfully verified CelestCombatPro state tracking!");
        }

        if (isHooked || celestCombatHooked) {
            Bukkit.getPluginManager().registerEvents(this, plugin);
        }

        plugin.getRewardsManager().registerProvider(this);
    }

    @Override
    public void disable() {
        if (!isHooked) return;
        plugin.getLogger().info("[Platinum] Safely disconnecting AuraSkills Bridge.");
    }

    private void registerCustomStats() {
        plugin.getLogger().info("[Platinum] Registering custom server mod traits and modifiers...");
    }

    @Override
    public boolean shouldReward(Player player, String triggerType) {
        return "SKILL_LEVEL_UP".equalsIgnoreCase(triggerType);
    }

    @Override
    public void grantRewards(Player player, Map<String, Object> context) {
        double rewardValue = (double) context.getOrDefault("calculated_value", 10.0);
        int itemAmount = (int) Math.max(1, Math.min(64, Math.round(rewardValue)));
        player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, itemAmount));

        // Modernized message using Adventure API
        player.sendMessage(Component.text("✦ [PlatinumBridge] ", NamedTextColor.GOLD)
                .append(Component.text("Consolidated Reward granted!", NamedTextColor.GREEN)));
    }

    @EventHandler
    public void onSkillLevelUp(SkillLevelUpEvent event) {
        if (!isHooked) return;

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        String skillName = event.getSkill().toString();
        int newLevel = event.getLevel();

        SkillsUser user = auraSkillsApi.getUserManager().getUser(uuid);
        if (user != null) {
            int totalSkillsCombined = 0;
            for (var skill : auraSkillsApi.getGlobalRegistry().getSkills()) {
                totalSkillsCombined += user.getSkillLevel(skill);
            }
            DataCache.setCachedSkillPoints(uuid, totalSkillsCombined);
        }

        double eldenMod = DataCache.getEldenMultiplier(uuid);
        if (eldenMod > 1.0) {
            player.sendMessage(Component.text("[Platinum] ", NamedTextColor.AQUA).decorate(TextDecoration.BOLD)
                    .append(Component.text("Your Elden Multiplier (" + eldenMod + "x) is augmenting your RPG scaling!", NamedTextColor.LIGHT_PURPLE)));
        }

        if (celestCombatHooked && isPlayerInCombat(player)) {
            player.sendMessage(Component.text("[Platinum] ", NamedTextColor.AQUA).decorate(TextDecoration.BOLD)
                    .append(Component.text("You leveled up " + skillName + " to " + newLevel + ", but you are currently marked in combat!", NamedTextColor.RED)));
            return;
        }

        if (newLevel % 5 == 0) {
            player.sendMessage(Component.text("[Platinum] ", NamedTextColor.AQUA).decorate(TextDecoration.BOLD)
                    .append(Component.text("Awesome job! You reached level " + newLevel + " in " + skillName + "!", NamedTextColor.YELLOW)));
        }
    }

    public double getPlayerStat(UUID playerUUID, String statName) {
        if (!isHooked) return 0.0;

        try {
            SkillsUser user = auraSkillsApi.getUserManager().getUser(playerUUID);
            if (user != null) {
                Stat targetStat = auraSkillsApi.getGlobalRegistry().getStat(NamespacedId.of("auraskills", statName.toLowerCase()));
                if (targetStat != null) {
                    return user.getStatLevel(targetStat);
                }
            }
        } catch (Exception e) {
            if (!errorLogged) {
                plugin.getLogger().warning("[Progress] Couldn't fetch AuraSkills stat: " + statName + ". Using default.");
                errorLogged = true;
            }
            return 0.0;
        }
        return 0.0;
    }

    public boolean isPlayerInCombat(Player player) {
        if (!celestCombatHooked || player == null) return false;
        return player.hasMetadata("combat-tagged") || player.hasMetadata("tagged");
    }

    public boolean isHooked() {
        return isHooked;
    }
}