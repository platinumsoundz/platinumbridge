package com.platinum.manager;

import com.platinum.Platinum;
import com.platinum.api.RewardProvider;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RewardsManager {
    private final Platinum plugin;
    private final List<RewardProvider> providers = new ArrayList<>();

    public RewardsManager(Platinum plugin) {
        this.plugin = plugin;
    }

    // --- The "Calculator": Your existing logic ---
    public double calculateReward(Player player) {
        double baseReward = 10.0;
        double multiplier = 1.0;

        // Fetch stats from your existing handlers
        double steps = plugin.getMechanicsManager().getStepCounterHandler().getSteps(player);

        // Logic: Multiplier based on activity
        if (steps > 5000) multiplier += 0.5; // Explorer bonus
        if (steps > 10000) multiplier += 1.0; // Master trekker bonus

        return baseReward * multiplier;
    }

    // --- The "Messenger": Unified Reward Execution ---

    public void registerProvider(RewardProvider provider) {
        if (!providers.contains(provider)) {
            providers.add(provider);
        }
    }

    /**
     * Triggers rewards across all registered bridge integrations.
     * Uses the calculated reward amount as part of the context.
     */
    public void distributeRewards(Player player, String triggerType, Map<String, Object> context) {
        // Calculate the base reward value
        double rewardValue = calculateReward(player);

        // Add the calculated value to the context so bridges can use it
        context.put("calculated_value", rewardValue);

        // Execute all providers that qualify for this trigger
        for (RewardProvider provider : providers) {
            if (provider.shouldReward(player, triggerType)) {
                provider.grantRewards(player, context);
            }
        }
    }
}