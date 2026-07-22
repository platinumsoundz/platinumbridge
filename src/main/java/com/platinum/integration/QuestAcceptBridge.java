package com.platinum.integration;

import com.magmaguy.elitemobs.api.QuestAcceptEvent;
import com.platinum.Platinum;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;
import java.lang.reflect.Method;

public class QuestAcceptBridge implements ServerBridge, Listener {

    private final Platinum plugin;

    public QuestAcceptBridge(Platinum plugin) {
        this.plugin = plugin;
    }

    @Override
    public void init() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        plugin.getLogger().info("QuestAcceptBridge initialized.");
    }

    @Override
    public void disable() {
        // No specific shutdown logic needed for this listener
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onQuestAccept(QuestAcceptEvent event) {
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

        player.setMetadata("accepted_quest_" + questId, new FixedMetadataValue(plugin, true));
    }
}