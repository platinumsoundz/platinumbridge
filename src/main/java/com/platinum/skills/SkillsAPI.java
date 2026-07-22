package com.platinum.skills;

import dev.aurelium.auraskills.api.AuraSkillsApi;
import dev.aurelium.auraskills.api.skill.Skill;
import dev.aurelium.auraskills.api.user.SkillsUser;
import org.bukkit.entity.Player;

public class SkillsAPI {

    /**
     * Gets the total skill levels a player has earned across all active skills.
     */
    public static int getTotalSkillPoints(Player player) {
        try {
            AuraSkillsApi api = AuraSkillsApi.get();
            SkillsUser user = api.getUserManager().getUser(player.getUniqueId());

            if (user != null) {
                int totalPoints = 0;

                // Loop through every registered skill on the server (Mining, Foraging, Combat, etc.)
                for (Skill skill : api.getGlobalRegistry().getSkills()) {
                    // Add the player's level in that specific skill to the total counter
                    totalPoints += user.getSkillLevel(skill);
                }

                return totalPoints;
            }
        } catch (Exception e) {
            // Fallback if AuraSkills fails
            return 0;
        }
        return 0;
    }
}