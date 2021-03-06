package io.github.cloudate9.passwordlogon.listeners;

import io.github.cloudate9.passwordlogon.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerLeave implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void teleportBack(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        if (Utils.noPasswordEntered.containsKey(p.getUniqueId())) {
            Utils.plugin.getConfig().set("location." + p.getUniqueId(), Utils.noPasswordEntered.get(p.getUniqueId()));
            Utils.plugin.getConfig().options().copyHeader(true);
            Utils.plugin.saveConfig();
            Utils.noPasswordEntered.remove(p.getUniqueId());
            return;
        }
        Utils.plugin.getConfig().set("location." + p.getUniqueId(), p.getLocation());
    }
}
