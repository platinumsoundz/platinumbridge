package com.platinum.mechanics;

import com.platinum.Platinum;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.HashMap;
import java.util.UUID;

public class CrossbowGunHandler implements Listener {
    private final Platinum plugin;
    private final HashMap<UUID, Boolean> isReloading = new HashMap<>();

    public CrossbowGunHandler(Platinum plugin) { this.plugin = plugin; }

    @EventHandler
    public void onShoot(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        ItemStack bow = event.getBow();
        if (bow == null || bow.getType() != Material.CROSSBOW) return;

        Player player = (Player) event.getEntity();
        if (getInventoryArrowCount(player) > 0) {
            removeOneArrow(player);
        }
    }

    private int getInventoryArrowCount(Player player) {
        int count = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == Material.ARROW) count += item.getAmount();
        }
        return count;
    }

    private void removeOneArrow(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == Material.ARROW) {
                item.setAmount(item.getAmount() - 1);
                break;
            }
        }
    }
}