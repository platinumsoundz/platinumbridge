package com.platinum.integration;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class EssentialsXBridge implements ServerBridge {

    private final Plugin plugin;
    private IEssentials essentials;
    private boolean isHooked = false;

    public EssentialsXBridge(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void init() {
        Plugin essPlugin = Bukkit.getPluginManager().getPlugin("Essentials");
        if (essPlugin instanceof Essentials) {
            this.essentials = (IEssentials) essPlugin;
            this.isHooked = true;
            plugin.getLogger().info("[Platinum] Successfully hooked into EssentialsX API!");
        } else {
            plugin.getLogger().warning("[Platinum] EssentialsX not found. EssentialsXBridge disabled.");
        }
    }

    /**
     * Gets the player's nickname, falling back to their username if none is set.
     */
    public String getPlayerNickname(Player player) {
        if (!isHooked) return player.getName();
        return essentials.getUser(player).getNickname();
    }

    /**
     * Checks if the player is currently AFK.
     */
    public boolean isPlayerAfk(Player player) {
        if (!isHooked) return false;
        return essentials.getUser(player).isAfk();
    }

    /**
     * Retrieves the player's home count (useful for UI/Menu stats).
     */
    public int getHomeCount(Player player) {
        if (!isHooked) return 0;
        return essentials.getUser(player).getHomes().size();
    }

    @Override
    public void disable() {
        this.isHooked = false;
        this.essentials = null;
    }
}