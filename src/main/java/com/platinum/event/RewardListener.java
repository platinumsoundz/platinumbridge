package com.platinum.event;

import com.platinum.Platinum;
import dev.aurelium.auraskills.api.event.skill.SkillLevelUpEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale; // Added import

public class RewardListener implements Listener {
    private final Platinum plugin;

    public RewardListener(Platinum plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAuraSkillsLevelUp(SkillLevelUpEvent event) {
        Map<String, Object> context = new HashMap<>();
        // Fixed: Added Locale.ENGLISH to getDisplayName
        context.put("skill", event.getSkill().getDisplayName(Locale.ENGLISH));
        context.put("new_level", event.getLevel());

        plugin.getRewardsManager().distributeRewards(
                event.getPlayer(),
                "SKILL_LEVEL_UP",
                context
        );
    }
}