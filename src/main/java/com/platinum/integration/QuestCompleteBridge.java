package com.platinum.integration;

import com.magmaguy.elitemobs.api.QuestCompleteEvent;
import com.platinum.Platinum;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.lang.reflect.Method;

// Now explicitly implements ServerBridge and Listener
public class QuestCompleteBridge implements ServerBridge, Listener {

    private final Platinum plugin;

    public QuestCompleteBridge(Platinum plugin) {
        this.plugin = plugin;
    }

    @Override
    public void init() {
        // Register the listener when the bridge initializes
        Bukkit.getPluginManager().registerEvents(this, plugin);
        plugin.getLogger().info("QuestCompleteBridge initialized.");
    }

    @Override
    public void disable() {
        // Logic for cleaning up if necessary upon server shutdown
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuestComplete(QuestCompleteEvent event) {
        Player player = event.getPlayer();
        if (event.getQuest() == null) return;

        String questId = "unknown";
        Object quest = event.getQuest();
        try {
            for (Method m : quest.getClass().getDeclaredMethods()) {
                if ((m.getName().equals("getId") || m.getName().equals("getName") || m.getName().equals("getQuestName"))
                        && m.getParameterCount() == 0) {
                    questId = String.valueOf(m.invoke(quest));
                    break;
                }
            }
        } catch (Exception e) {
            questId = quest.toString();
        }

        final String finalQuestId = questId;
        Bukkit.getScheduler().runTask(plugin, () -> {
            if (player.isOnline()) {
                player.removeMetadata("accepted_quest_" + finalQuestId, plugin);
            }
        });
    }
}