package com.cloudate9.passwordlogon.listeners;

import com.cloudate9.passwordlogon.auth.PlayerStatus;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PrepareForAuth implements Listener {

    private final Plugin plugin;


    public PrepareForAuth(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        PlayerStatus.addNonAuthPlayer(e.getPlayer().getName(), e.getPlayer().getLocation());
        e.getPlayer().setGameMode(GameMode.SPECTATOR);

        Location teleportToLocation = switch (e.getPlayer().getWorld().getEnvironment()) {
            case NETHER -> plugin.getConfig().getLocation("location.nether");
            case THE_END -> plugin.getConfig().getLocation("location.end");
            default -> plugin.getConfig().getLocation("location.overworld");
        };

        assert teleportToLocation != null;

        // Delay teleport by 1 tick so that server logs show the player's correct log in location
        new BukkitRunnable() {
            @Override
            public void run() {
                e.getPlayer().teleport(teleportToLocation);
            }
        }.runTaskLater(plugin, 1L);
    }

}
