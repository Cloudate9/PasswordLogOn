package me.awesomemoder316.passwordlogon.listeners;

import me.awesomemoder316.passwordlogon.MessageConfig;
import me.awesomemoder316.passwordlogon.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class OnPlayerChat implements Listener {

    @EventHandler
    public void enterPassword(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();

        if (Utils.noPasswordEntered.containsKey(player.getUniqueId())) {
            e.setCancelled(true);

            if (Utils.plugin.getConfig().contains("password." + player.getUniqueId())) {

                if (e.getMessage().equals(Utils.plugin.getConfig().get("password." + player.getUniqueId()))) {
                    player.playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRIGGER, 1, 0);
                    Utils.teleportEffects(player);
                    new MessageConfig().successfulLogIn(player);

                    Utils.teleportBack(player);
                    //Player removed from Utils.noPasswordEntered in Utils.teleportBack(player)


                } else {
                    new MessageConfig().failedLogIn(player);
                }
            } else {
                new MessageConfig().noPasswordSet(player);
            }
        }
    }
}