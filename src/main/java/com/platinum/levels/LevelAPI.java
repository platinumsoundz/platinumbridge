package com.platinum.levels;

import dev.aurelium.auraskills.api.AuraSkillsApi;
import dev.aurelium.auraskills.api.user.SkillsUser;
import org.bukkit.entity.Player;

public class LevelAPI {

    /**
     * Gets the custom level of a player based on their AuraSkills Power Level.
     */
    public static int getPlayerLevel(Player player) {
        try {
            // Get the running instance of AuraSkills
            AuraSkillsApi api = AuraSkillsApi.get();

            // Fetch the user profile for the player using their UUID
            SkillsUser user = api.getUserManager().getUser(player.getUniqueId());

            if (user != null) {
                // Returns the sum of all skill levels (often used as global level/power level)
                return user.getPowerLevel();
            }
        } catch (Exception e) {
            // Fallback if AuraSkills isn't loaded or fails
            return 1;
        }
        return 1;
    }
}