package com.platinum.mechanics;

import com.platinum.Platinum;
import org.bukkit.block.BlockFace; // Corrected import
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import java.util.HashMap;
import java.util.UUID;

public class StepCounterHandler implements Listener {
    private final Platinum plugin;
    private final HashMap<UUID, Double> stepCount = new HashMap<>();

    public StepCounterHandler(Platinum plugin) { this.plugin = plugin; }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getTo() == null) return;
        Player player = event.getPlayer();

        // Check if the block below the player is NOT air to verify they are on ground[cite: 14]
        if (!player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isAir()) {
            double distance = event.getFrom().distance(event.getTo());
            UUID uuid = player.getUniqueId();
            stepCount.put(uuid, stepCount.getOrDefault(uuid, 0.0) + distance);
        }
    }

    public double getSteps(Player player) { return stepCount.getOrDefault(player.getUniqueId(), 0.0); }
}