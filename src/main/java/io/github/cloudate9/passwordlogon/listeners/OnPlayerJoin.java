package io.github.cloudate9.passwordlogon.listeners;

import io.github.cloudate9.passwordlogon.MessageConfig;
import io.github.cloudate9.passwordlogon.Utils;
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

        Bukkit.getScheduler().scheduleSyncDelayedTask(
                Utils.plugin, () -> askPassword(e.getPlayer()), 1);
    }

    private static void askPassword(Player p) {
        Utils.defineBedrockPos(p);
        if (Utils.plugin.getConfig().contains("location." + p.getUniqueId()))
            Utils.noPasswordEntered.put(
                    p.getUniqueId(),
                    Utils.plugin.getConfig().getLocation("location." + p.getUniqueId())
            );
        else Utils.noPasswordEntered.put(p.getUniqueId(), p.getLocation());

        p.teleport(Utils.teleportTo(p.getWorld()));

        if (Utils.plugin.getConfig().contains("password." + p.getUniqueId())) {
            new MessageConfig().loginPrompt(p);
        } else {
            new MessageConfig().passwordCreatePrompt(p);
        }
    }
}
