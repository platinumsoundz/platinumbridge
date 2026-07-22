package com.platinum.integration;

import com.platinum.Platinum;
import com.platinum.api.RewardProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.Map;

public class SkillTreeBridge implements ServerBridge, RewardProvider {

    private final Platinum plugin;

    public SkillTreeBridge(Platinum plugin) {
        this.plugin = plugin;
    }

    @Override
    public void init() {
        System.out.println("[Platinum-Elden] Successfully hooked into SkillTree!");
        plugin.getRewardsManager().registerProvider(this);
    }

    // This method was missing, causing the compilation error
    @Override
    public void disable() {
        // Add cleanup logic here if needed
    }

    // --- RewardProvider Implementation ---
    @Override
    public boolean shouldReward(Player player, String triggerType) {
        return "SKILL_TREE_MILESTONE".equalsIgnoreCase(triggerType);
    }

    @Override
    public void grantRewards(Player player, Map<String, Object> context) {
        double rewardValue = (double) context.getOrDefault("calculated_value", 5.0);

        player.getInventory().addItem(new ItemStack(Material.EXPERIENCE_BOTTLE, (int) Math.round(rewardValue)));

        player.sendMessage(Component.text("◈ [SkillTree] ", NamedTextColor.DARK_PURPLE)
                .append(Component.text("Skill progression rewards synchronized!", NamedTextColor.WHITE)));
    }
}