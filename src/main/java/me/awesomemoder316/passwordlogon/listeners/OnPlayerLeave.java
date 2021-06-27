package me.awesomemoder316.passwordlogon.listeners;

import me.awesomemoder316.passwordlogon.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class OnPlayerLeave implements Listener {

    @EventHandler
    public void logLocation(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        if (Utils.noPasswordEntered.containsKey(p.getUniqueId())) {
            p.teleport(Utils.noPasswordEntered.get(p.getUniqueId()));
            Utils.noPasswordEntered.remove(p.getUniqueId());
        }
    }
}
