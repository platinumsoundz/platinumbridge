package com.platinum.api;

import org.bukkit.entity.Player;
import java.util.Map;

/**
 * Interface to be implemented by bridge classes that provide rewards
 * to be processed by the RewardsManager.
 */
public interface RewardProvider {

    /**
     * Determines if the specific provider should grant rewards for the given trigger.
     * * @param player The player involved in the event.
     * @param triggerType The type of event (e.g., "LEVEL_UP", "QUEST_COMPLETE").
     * @return true if rewards should be processed.
     */
    boolean shouldReward(Player player, String triggerType);

    /**
     * Executes the reward logic.
     * * @param player The player receiving the rewards.
     * @param context Additional metadata (e.g., level reached, quest ID).
     */
    void grantRewards(Player player, Map<String, Object> context);
}