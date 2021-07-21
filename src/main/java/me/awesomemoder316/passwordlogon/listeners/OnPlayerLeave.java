package me.awesomemoder316.passwordlogon.listeners;

import me.awesomemoder316.passwordlogon.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerLeave implements Listener {

    @EventHandler
    public void teleportBack(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        if (Utils.noPasswordEntered.containsKey(p.getUniqueId())) {
            p.teleport(Utils.noPasswordEntered.get(p.getUniqueId()));
            Utils.noPasswordEntered.remove(p.getUniqueId());
        }
    }
}
