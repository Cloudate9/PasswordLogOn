package io.github.awesomemoder316.passwordlogon.listeners;

import io.github.awesomemoder316.passwordlogon.MessageConfig;
import io.github.awesomemoder316.passwordlogon.Utils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class OnPlayerChat implements Listener {

    @EventHandler
    public void enterPassword(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();

        if (Utils.noPasswordEntered.containsKey(player.getUniqueId())) {
            e.setCancelled(true);

            if (Utils.plugin.getConfig().contains("password." + player.getUniqueId())) {

                if (e.getMessage().equals(Utils.plugin.getConfig().get("password." + player.getUniqueId()))) {
                    player.playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRIGGER, 1, 0);
                    teleportEffects(player);
                    new MessageConfig().successfulLogIn(player);

                    teleportBack(Utils.noPasswordEntered.get(e.getPlayer().getUniqueId()), player);
                    Utils.noPasswordEntered.remove(e.getPlayer().getUniqueId());

                } else {
                    new MessageConfig().failedLogIn(player);
                }
            } else {
                new MessageConfig().noPasswordSet(player);
            }
        }
    }

    public static void teleportEffects(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 180, 30));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 254));
                    }
                }.runTaskLater(Utils.plugin, 120L);
            }
        }.runTaskLater(Utils.plugin, 0L);
    }

    private void teleportBack(Location loc, Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                player.teleport(loc);
                player.playSound(player.getLocation(), Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1, 1);
            }
        }.runTaskLater(Utils.plugin, 120L);
    }
}
