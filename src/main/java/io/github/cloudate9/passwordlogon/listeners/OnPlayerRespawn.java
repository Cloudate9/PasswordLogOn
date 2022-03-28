package io.github.cloudate9.passwordlogon.listeners;

import io.github.cloudate9.passwordlogon.MessageConfig;
import io.github.cloudate9.passwordlogon.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class OnPlayerRespawn implements Listener {
    @EventHandler
    public void whenRespawn(PlayerRespawnEvent e) {
        if (Utils.noPasswordEntered.containsKey(e.getPlayer().getUniqueId())) {
            Utils.defineBedrockPos(e.getPlayer());
            e.getPlayer().teleport(Utils.teleportTo(e.getPlayer().getWorld()));
            new MessageConfig().loginPrompt(e.getPlayer());
        }
    }
}
