package com.platinum.mechanics;

import com.platinum.Platinum;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class GrappleHandler implements Listener {
    private final Platinum plugin;
    private final LegacyComponentSerializer serializer = LegacyComponentSerializer.legacySection();

    public GrappleHandler(Platinum plugin) { this.plugin = plugin; }

    @EventHandler
    public void onGrapple(PlayerFishEvent event) {
        Player player = event.getPlayer();
        ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();

        if (meta != null && meta.hasDisplayName()) {
            String name = serializer.serialize(meta.displayName());
            if ("§bGrappling Hook".equals(name)) {
                if (event.getState() == PlayerFishEvent.State.REEL_IN || event.getState() == PlayerFishEvent.State.IN_GROUND) {
                    Vector direction = event.getHook().getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
                    double multiplier = plugin.getConfig().getDouble("mechanics.grapple.multiplier", 1.5);
                    player.setVelocity(direction.multiply(multiplier));
                }
            }
        }
    }
}