package com.platinum.mechanics;

import com.platinum.Platinum;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.UUID;

public class AttackHandler implements Listener {
    private final Platinum plugin;
    private final HashMap<UUID, Long> attackCooldowns = new HashMap<>();
    private final LegacyComponentSerializer serializer = LegacyComponentSerializer.legacySection();

    public AttackHandler(Platinum plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onWeaponAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;

        ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
        if (meta != null && meta.hasDisplayName()) {
            // Modernized display name check
            String itemName = serializer.serialize(meta.displayName());

            if ("§6Platinum Sword".equals(itemName)) {
                String strengthRaw = plugin.getBridgeManager().getPlaceholderValue(player, "%auraskills_strength%");
                double strengthLevel = 1.0;
                try { strengthLevel = Double.parseDouble(strengthRaw); } catch (Exception e) { strengthLevel = 1.0; }

                double multiplier = plugin.getConfig().getDouble("mechanics.attack.sword-multiplier", 1.5) + (strengthLevel * 0.01);
                event.setDamage(event.getDamage() * multiplier);

                if (System.currentTimeMillis() - attackCooldowns.getOrDefault(player.getUniqueId(), 0L) > 2000) {
                    event.getEntity().setVelocity(player.getLocation().getDirection().multiply(1.2));
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0f, 1.0f);
                    attackCooldowns.put(player.getUniqueId(), System.currentTimeMillis());
                }
            }
        }
    }
}