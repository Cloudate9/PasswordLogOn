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
            case NETHER -> new Location(
                    e.getPlayer().getWorld(),
                    plugin.getConfig().getDouble("location.nether.x"),
                    plugin.getConfig().getDouble("location.nether.y"),
                    plugin.getConfig().getDouble("location.nether.z")
            );
            case THE_END -> new Location(
                    e.getPlayer().getWorld(),
                    plugin.getConfig().getDouble("location.end.x"),
                    plugin.getConfig().getDouble("location.end.y"),
                    plugin.getConfig().getDouble("location.end.z")
            );
            default -> new Location(
                    e.getPlayer().getWorld(),
                    plugin.getConfig().getDouble("location.overworld.x"),
                    plugin.getConfig().getDouble("location.overworld.y"),
                    plugin.getConfig().getDouble("location.overworld.z")
            );
        };

        // Delay teleport by 1 tick so that server logs show the player's correct log in location
        new BukkitRunnable() {
            @Override
            public void run() {
                e.getPlayer().teleport(teleportToLocation);
            }
        }.runTaskLater(plugin, 1L);
    }

}
