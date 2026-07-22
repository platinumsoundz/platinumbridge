package com.platinum.placeholder;

import com.platinum.Platinum;
import com.platinum.mechanics.StepCounterHandler;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class StepPlaceholderExpansion extends PlaceholderExpansion {
    private final Platinum plugin;
    private final StepCounterHandler stepHandler;

    public StepPlaceholderExpansion(Platinum plugin, StepCounterHandler stepHandler) {
        this.plugin = plugin;
        this.stepHandler = stepHandler;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "platinum";
    }

    @Override
    public @NotNull String getAuthor() {
        return "PlatinumDeveloper";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) return null;

        if (params.equals("steps")) {
            return String.valueOf((int) stepHandler.getSteps(player.getPlayer()));
        }

        return null;
    }
}