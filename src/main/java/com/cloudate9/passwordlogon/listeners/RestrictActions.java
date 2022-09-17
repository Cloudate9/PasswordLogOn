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

public class RestrictActions implements Listener {

    private final MessageConfig messageConfig;
    private final MiniMessage miniMessage;

    public RestrictActions(MessageConfig messageConfig, MiniMessage miniMessage) {
        this.messageConfig = messageConfig;
        this.miniMessage = miniMessage;
    }

    @EventHandler
    public void restrictMovement(PlayerMoveEvent e) {
        if (e.hasChangedBlock() && PlayerStatus.getNonAuthPlayers().containsKey(e.getPlayer().getName())) e.setCancelled(true);
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

}
