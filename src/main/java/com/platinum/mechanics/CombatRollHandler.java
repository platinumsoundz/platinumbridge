package com.platinum.mechanics;

import com.platinum.Platinum;
import org.bukkit.block.BlockFace; // Corrected import path
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CombatRollHandler implements Listener {
    private final Platinum plugin;
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    public CombatRollHandler(Platinum plugin) { this.plugin = plugin; }

    @EventHandler
    public void onCombatRoll(PlayerToggleSneakEvent event) {
        if (!event.isSneaking()) return;

        Player player = event.getPlayer();

        if (cooldowns.containsKey(player.getUniqueId()) &&
                (System.currentTimeMillis() - cooldowns.get(player.getUniqueId()) < 500)) {
            return;
        }

        // Updated check: verify if the block below is air[cite: 11, 12]
        if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isAir()) {
            cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
            performRoll(player);
        }
    }

    private void performRoll(Player player) {
        String agilityRaw = plugin.getBridgeManager().getPlaceholderValue(player, "%auraskills_agility%");
        double agilityLevel = 1.0;
        try { agilityLevel = Double.parseDouble(agilityRaw); } catch (Exception e) { agilityLevel = 1.0; }

        double finalMultiplier = plugin.getConfig().getDouble("mechanics.combat-roll.multiplier", 1.2) + (agilityLevel * 0.02);

        Vector direction = player.getVelocity().length() > 0.1
                ? player.getVelocity().setY(0).normalize()
                : player.getLocation().getDirection().setY(0).normalize();

        player.setVelocity(direction.multiply(finalMultiplier));
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 1));
    }
}