package com.platinum.menu;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

public class PlatinumMenu implements Listener {

    private static final int MENU_SLOT = 8;
    private static final Material MENU_ITEM = Material.NETHER_STAR;
    private static final String ITEM_ID = "platinum_menu_item";
    private static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer.legacySection();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) { giveMenuIcon(event.getPlayer()); }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) { giveMenuIcon(event.getPlayer()); }

    private void giveMenuIcon(org.bukkit.entity.Player player) {
        ItemStack item = new ItemStack(MENU_ITEM);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(SERIALIZER.deserialize("§dPlatinum Menu"));
        meta.lore(Collections.singletonList(Component.text(ITEM_ID)));
        item.setItemMeta(meta);
        player.getInventory().setItem(MENU_SLOT, item);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getCurrentItem() != null && isPlatinumItem(event.getCurrentItem())) event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {
        if (isPlatinumItem(event.getItemDrop().getItemStack())) event.setCancelled(true);
    }

    @EventHandler
    public void onItemClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getItem() != null && isPlatinumItem(event.getItem())) {
                // Changed command to launch shop_menu directly[cite: 2]
                event.getPlayer().performCommand("shop_menu");
            }
        }
    }

    private boolean isPlatinumItem(ItemStack item) {
        if (item.getType() != MENU_ITEM || !item.hasItemMeta()) return false;
        List<Component> lore = item.getItemMeta().lore();
        if (lore == null) return false;
        return lore.stream().anyMatch(comp -> SERIALIZER.serialize(comp).contains(ITEM_ID));
    }
}