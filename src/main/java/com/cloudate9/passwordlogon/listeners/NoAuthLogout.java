package com.cloudate9.passwordlogon.listeners;

import com.cloudate9.passwordlogon.auth.PlayerStatus;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class NoAuthLogout implements Listener {

    @EventHandler
    public void noAuthLoggedOut(PlayerQuitEvent e) {
        if (PlayerStatus.getNonAuthPlayers().containsKey(e.getPlayer().getName())) {
            e.getPlayer().teleport(PlayerStatus.getNonAuthPlayers().get(e.getPlayer().getName()));
        }
    }

}
