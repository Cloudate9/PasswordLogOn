package com.cloudate9.passwordlogon.listeners;

import com.cloudate9.passwordlogon.auth.PlayerStatus;
import com.cloudate9.passwordlogon.messages.MessageConfig;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;

public class RestrictActions implements Listener {

    private final MessageConfig messageConfig;
    private final MiniMessage miniMessage;
    private final Plugin plugin;

    public RestrictActions(MessageConfig messageConfig, MiniMessage miniMessage, Plugin plugin) {
        this.messageConfig = messageConfig;
        this.miniMessage = miniMessage;
        this.plugin = plugin;
    }

    @EventHandler
    public void restrictMovement(PlayerMoveEvent e) {
        if (!e.hasChangedBlock() || !PlayerStatus.getNonAuthPlayers().containsKey(e.getPlayer().getName())) return;

        // TODO Logic to check if non-auth player has gotten OOB, and if so, make them go back to a in bounds region
    }

    @EventHandler
    public void restrictInventory(InventoryOpenEvent e) {
        if (PlayerStatus.getNonAuthPlayers().containsKey(e.getPlayer().getName())) e.setCancelled(true);
    }

    @EventHandler
    public void restrictChat(AsyncChatEvent e) {
        if (PlayerStatus.getNonAuthPlayers().containsKey(e.getPlayer().getName())) e.setCancelled(true);
        Component explainedRestrictedChat = miniMessage.deserializeOrNull(
                messageConfig.getOrCreateConfig().getString("password_prompt"));
        if (explainedRestrictedChat != null) e.getPlayer().sendMessage(explainedRestrictedChat);
    }

    @EventHandler
    public void restrictSpectatorTeleport(PlayerTeleportEvent e) {
        // TODO Check if this restricting of teleportation actually works
        if (PlayerStatus.getNonAuthPlayers().containsKey(e.getPlayer().getName())) e.setCancelled(true);
    }

}
