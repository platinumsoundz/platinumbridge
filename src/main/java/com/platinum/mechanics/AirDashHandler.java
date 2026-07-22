package com.platinum.mechanics;

import com.platinum.Platinum;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AirDashHandler implements Listener {
    private final Platinum plugin;
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    public AirDashHandler(Platinum plugin) { this.plugin = plugin; }

    @EventHandler
    public void onAirCrouch(PlayerToggleSneakEvent event) {
        if (!event.isSneaking()) return;

        Player player = event.getPlayer();

        if (cooldowns.containsKey(player.getUniqueId()) &&
                (System.currentTimeMillis() - cooldowns.get(player.getUniqueId()) < 500)) {
            return;
        }

        // Updated check: verify if the block below is air
        if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isAir()) {
            cooldowns.put(player.getUniqueId(), System.currentTimeMillis());

            double agility = getAgility(player);
            double multiplier = plugin.getConfig().getDouble("mechanics.air-dash.multiplier", 1.2) + (agility * 0.03);
            double downwardForce = plugin.getConfig().getDouble("mechanics.air-dash.downward-force", -0.2);

            Vector dash = player.getLocation().getDirection().normalize().multiply(multiplier);
            dash.setY(downwardForce);
            player.setVelocity(dash);
        }
    }

    private double getAgility(Player player) {
        try { return Double.parseDouble(plugin.getBridgeManager().getPlaceholderValue(player, "%auraskills_agility%")); }
        catch (Exception e) { return 1.0; }
    }
}