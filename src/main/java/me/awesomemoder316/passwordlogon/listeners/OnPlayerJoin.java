package me.awesomemoder316.passwordlogon.listeners;

import me.awesomemoder316.passwordlogon.Utils;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class OnPlayerJoin implements Listener {

    @EventHandler
    public void informUpdateAndGetPassword(PlayerJoinEvent e) {
        if (Utils.update) { // Start get update
            if (e.getPlayer().isOp()) {
                e.getPlayer().sendMessage(ChatColor.BLUE + "A new version is available at https://www.spigotmc.org/resources/password-log-in.90958/!");
            }
        }

        Utils.askPassword(e.getPlayer());
    }
}
