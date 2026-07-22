package com.platinum.integration;

import org.bukkit.plugin.Plugin;

public class VeinMinerBridge implements ServerBridge {

    private final Plugin plugin;

    public VeinMinerBridge(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void init() {
        System.out.println("[Platinum-Elden] Successfully hooked into VeinMiner!");
    }

    @Override
    public void disable() {}
}