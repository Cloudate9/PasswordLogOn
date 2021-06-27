package me.awesomemoder316.passwordlogon.listeners;

import me.awesomemoder316.passwordlogon.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.UUID;

public class OnPlayerRespawn implements Listener {
    @EventHandler
    public void whenRespawn(PlayerRespawnEvent e) {
        if (Utils.noPasswordEntered.containsKey(e.getPlayer().getUniqueId())) {
            Utils.defineBedrockPos(e.getPlayer());
            e.getPlayer().teleport(new Location(e.getPlayer().getWorld(), 0, 80, 0));
            e.getPlayer().sendMessage(ChatColor.RED + "You still need to enter your password in chat!");
        }
    }
}
