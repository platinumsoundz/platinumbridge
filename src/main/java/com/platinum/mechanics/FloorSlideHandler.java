package com.platinum.mechanics;

import com.platinum.Platinum;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FloorSlideHandler implements Listener {

    private final Platinum plugin;
    private final Map<UUID, Long> lastSneak = new HashMap<>();

    public FloorSlideHandler(Platinum plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSprintCrouch(PlayerToggleSneakEvent event) {
        if (!event.isSneaking()) return;

        Player player = event.getPlayer();
        if (player.isSprinting()) {
            long currentTime = System.currentTimeMillis();

            // Standardized 500ms cooldown window
            if (lastSneak.containsKey(player.getUniqueId()) && (currentTime - lastSneak.get(player.getUniqueId()) < 500)) {
                return;
            }

            String agilityRaw = plugin.getBridgeManager().getPlaceholderValue(player, "%auraskills_agility%");
            double agilityLevel = 0.0;
            try {
                agilityLevel = Double.parseDouble(agilityRaw);
            } catch (NumberFormatException e) {
                agilityLevel = 1.0;
            }

            double baseMultiplier = plugin.getConfig().getDouble("mechanics.floor-slide.multiplier", 1.5);
            double upwardForce = plugin.getConfig().getDouble("mechanics.floor-slide.upward-force", 0.1);
            double finalMultiplier = baseMultiplier + (agilityLevel * 0.02);

            Vector slide = player.getLocation().getDirection().normalize().multiply(finalMultiplier);
            slide.setY(upwardForce);
            player.setVelocity(slide);

            lastSneak.put(player.getUniqueId(), currentTime);
        }
    }
}