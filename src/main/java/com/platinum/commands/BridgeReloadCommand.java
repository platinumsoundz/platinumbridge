package com.platinum.commands;

import com.platinum.Platinum;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BridgeReloadCommand implements CommandExecutor {

    private final Platinum plugin;

    public BridgeReloadCommand(Platinum plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("platinum.admin")) {
            sender.sendMessage("§cYou do not have permission to do this.");
            return true;
        }

        plugin.getBridgeManager().reload();
        sender.sendMessage("§b[Platinum 3.0] §fBridge system reloaded successfully!");
        return true;
    }
}