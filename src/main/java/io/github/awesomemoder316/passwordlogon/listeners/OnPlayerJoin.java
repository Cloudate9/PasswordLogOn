package io.github.awesomemoder316.passwordlogon.listeners;

import io.github.awesomemoder316.passwordlogon.MessageConfig;
import io.github.awesomemoder316.passwordlogon.Utils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class OnPlayerJoin implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void informUpdateAndGetPassword(PlayerJoinEvent e) {
        if (Utils.update) { // Start get update
            if (e.getPlayer().isOp()) {
                e.getPlayer().sendMessage(ChatColor.GOLD + "[Password Log On] " + ChatColor.BLUE + "can be updated at https://www.curseforge.com/minecraft/bukkit-plugins/password-log-on!");
            }
        }

        askPassword(e.getPlayer());
    }

    private static void askPassword(Player p) {
        Utils.defineBedrockPos(p);
        Utils.noPasswordEntered.put(p.getUniqueId(), p.getLocation());

        p.teleport(Utils.teleportTo(p.getWorld()));

        if (Utils.plugin.getConfig().contains("password." + p.getUniqueId())) {
            new MessageConfig().loginPrompt(p);
        } else {
            new MessageConfig().passwordCreatePrompt(p);
        }
    }
}
