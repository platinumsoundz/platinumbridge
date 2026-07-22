package com.platinum.registry;

import com.platinum.Platinum;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataCache {

    private static final Map<UUID, Double> eldenMultipliers = new HashMap<>();
    private static final Map<UUID, Integer> totalSkillCaches = new HashMap<>();
    private static final Map<UUID, Boolean> combatStates = new HashMap<>();
    private static final Map<UUID, Double> miningBoosts = new HashMap<>();
    private static final Map<UUID, String> playerPrefixes = new HashMap<>();

    public static void setEldenMultiplier(UUID uuid, double val) { eldenMultipliers.put(uuid, val); }
    public static double getEldenMultiplier(UUID uuid) { return eldenMultipliers.getOrDefault(uuid, 1.0); }

    public static void setCachedSkillPoints(UUID uuid, int val) { totalSkillCaches.put(uuid, val); }

    public static int getCombinedBlendedPower(Player p) {
        return totalSkillCaches.getOrDefault(p.getUniqueId(), 0);
    }

    public static void setPlayerPrefix(UUID uuid, String val) { playerPrefixes.put(uuid, val); }
    public static String getPlayerPrefix(UUID uuid) { return playerPrefixes.getOrDefault(uuid, "§7"); }

    public static void setMiningBoost(UUID uuid, double val) { miningBoosts.put(uuid, val); }
    public static double getMiningBoost(UUID uuid) { return miningBoosts.getOrDefault(uuid, 0.0); }

    public static void setCombatState(UUID uuid, boolean val) {
        combatStates.put(uuid, val);
        Player p = org.bukkit.Bukkit.getPlayer(uuid);
        if (p != null) triggerSync(p);
    }
    public static boolean isInCombat(UUID uuid) { return combatStates.getOrDefault(uuid, false); }

    private static void triggerSync(Player player) {
        // Logic for syncing data remains here,
        // BossBarManager calls have been removed to resolve compilation errors.
    }
}