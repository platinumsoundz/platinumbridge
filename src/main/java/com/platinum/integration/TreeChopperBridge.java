package com.platinum.integration;

import org.bukkit.plugin.Plugin;

public class TreeChopperBridge implements ServerBridge {

    private final Plugin plugin;

    public TreeChopperBridge(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void init() {
        System.out.println("[Platinum-Elden] Successfully hooked into TreeChopper!");
    }

    @Override
    public void disable() {}
}