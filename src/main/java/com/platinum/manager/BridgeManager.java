package com.platinum.manager;

import com.platinum.Platinum;
import com.platinum.bridge.placeholders.PlatinumBridgeExpansion;
import com.platinum.integration.*;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;

public class BridgeManager {

    private final Platinum plugin;
    private final List<ServerBridge> activeBridges = new ArrayList<>();

    public BridgeManager(Platinum plugin) {
        this.plugin = plugin;
    }

    public String getPlaceholderValue(Player player, String placeholder) {
        if (plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            return PlaceholderAPI.setPlaceholders(player, placeholder);
        }
        return "0";
    }

    public void reload() {
        shutdown();
        init();
    }

    public void init() {
        // All these bridges are now RewardProviders that auto-register
        // with plugin.getRewardsManager() during their init() call
        ServerBridge[] bridges = {
                new AuraSkillsBridge(plugin),
                new CelestCombatBridge(plugin),
                new EconomyShopGUIBridge(plugin),
                new EliteMobsBridge(plugin),
                new EssentialsXBridge(plugin),
                new HereNavyBridge(plugin),
                new LevelledMobsBridge(plugin),
                new MiningBoostBridge(plugin),
                new QuestAcceptBridge(plugin),
                new QuestCompleteBridge(plugin),
                new RPGToolsBridge(plugin),
                new SimpleSurvivalRPGBridge(plugin),
                new SkillTreeBridge(plugin),
                new TreeChopperBridge(plugin),
                new VeinMinerBridge(plugin)
        };

        for (ServerBridge bridge : bridges) {
            try {
                bridge.init();
                activeBridges.add(bridge);
            } catch (Exception e) {
                plugin.getLogger().severe("[Platinum] Failed to initialize bridge: " + e.getMessage());
            }
        }

        if (plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlatinumBridgeExpansion(plugin).register();
            plugin.getLogger().info("[Platinum] PlaceholderAPI expansion registered.");
        }
    }

    public void shutdown() {
        for (ServerBridge bridge : activeBridges) {
            try {
                bridge.disable();
            } catch (Exception e) {
                plugin.getLogger().warning("[Platinum] Error disabling bridge: " + e.getMessage());
            }
        }
        activeBridges.clear();
    }
}