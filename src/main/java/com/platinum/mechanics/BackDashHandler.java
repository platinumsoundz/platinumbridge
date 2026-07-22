package com.platinum.mechanics;

import com.platinum.Platinum;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BackDashHandler implements Listener {
    private final Platinum plugin;
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    public BackDashHandler(Platinum plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDoubleJump(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == org.bukkit.GameMode.CREATIVE || player.getGameMode() == org.bukkit.GameMode.SPECTATOR) return;

        // Apply cooldown to prevent spam triggering
        if (cooldowns.containsKey(player.getUniqueId()) &&
                (System.currentTimeMillis() - cooldowns.get(player.getUniqueId()) < 500)) {
            return;
        }

        event.setCancelled(true);
        player.setFlying(false);
        player.setAllowFlight(false);
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis());

        String agilityRaw = plugin.getBridgeManager().getPlaceholderValue(player, "%auraskills_agility%");
        double agilityLevel = 0.0;
        try { agilityLevel = Double.parseDouble(agilityRaw); } catch (NumberFormatException e) { agilityLevel = 1.0; }

        double baseJump = plugin.getConfig().getDouble("mechanics.double-jump.base-height", 0.6);
        double finalJump = baseJump + (agilityLevel * 0.01);

        player.setVelocity(player.getVelocity().setY(finalJump));
    }
}