package com.platinum.bridge.placeholders;

import com.platinum.Platinum;
import com.platinum.registry.DataCache;
import com.platinum.levels.LevelAPI;
import com.platinum.skills.SkillsAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PlatinumBridgeExpansion extends PlaceholderExpansion {

    private final Platinum plugin;

    public PlatinumBridgeExpansion(Platinum plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() { return "platinumbridge"; }

    @Override
    public @NotNull String getAuthor() { return "Platinum"; }

    @Override
    public @NotNull String getVersion() { return "1.0.0"; }

    @Override
    public boolean persist() { return true; }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) return "";

        // 1. LEGACY CORE TOKENS
        if (params.equalsIgnoreCase("power")) {
            try {
                int baseLevel = LevelAPI.getPlayerLevel(player.getPlayer());
                int totalSkills = SkillsAPI.getTotalSkillPoints(player.getPlayer());
                return String.valueOf((baseLevel * 10) + totalSkills);
            } catch (Exception e) { return "0"; }
        }

        if (params.equalsIgnoreCase("rank")) {
            try {
                int baseLevel = LevelAPI.getPlayerLevel(player.getPlayer());
                if (baseLevel >= 50) return "§d[Mythic]";
                if (baseLevel >= 25) return "§b[Heroic]";
                return "§7[Adventurer]";
            } catch (Exception e) { return "§7[None]"; }
        }

        // 2. CROSS-PLUGIN COMBINED MATRIX TOKENS
        if (params.equalsIgnoreCase("combined_power")) {
            return String.valueOf(DataCache.getCombinedBlendedPower(player.getPlayer()));
        }

        if (params.equalsIgnoreCase("elden_mod")) {
            return String.valueOf(DataCache.getEldenMultiplier(player.getUniqueId()));
        }

        // 3. REMASTERED SYSTEM TRACKING TOKENS
        if (params.equalsIgnoreCase("prefix")) {
            return DataCache.getPlayerPrefix(player.getUniqueId());
        }

        if (params.equalsIgnoreCase("mining_boost")) {
            return String.valueOf(DataCache.getMiningBoost(player.getUniqueId()));
        }

        if (params.equalsIgnoreCase("combat_status")) {
            boolean inCombat = DataCache.isInCombat(player.getUniqueId());
            return inCombat ? "§c§lIN COMBAT" : "§a§lSAFE";
        }

        return null;
    }
}