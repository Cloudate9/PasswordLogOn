package io.github.awesomemoder316.passwordlogon;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;

public class Utils {
    public static boolean firstCheck = true;
    public static HashMap<UUID, Location> noPasswordEntered = new HashMap<>();
    public static boolean update = false;
    public static PasswordLogOn plugin;

    public static void defineBedrockPos(Player p) {
        String environment;

        switch (p.getWorld().getEnvironment()) {
            case NORMAL:
                environment = "overworld";
                break;

            case NETHER:
                environment = "nether";
                break;

            default:
                environment = "end";
                break;
        }

        double x1 = plugin.getConfig().getDouble(environment + ".x1");
        double x2 = plugin.getConfig().getDouble(environment + ".x2");
        double y = plugin.getConfig().getDouble(environment + ".y");
        double z1 = plugin.getConfig().getDouble(environment + ".z1");
        double z2 = plugin.getConfig().getDouble(environment + ".z2");

        if (x1 <= x2) {
            for (double xPos = x1; xPos < x2 + 1; xPos++) {

                if (z1 <= z2) {
                    for (double zPos = z1; zPos < z2 + 1; zPos++) {
                        setBedrock(new Location(p.getWorld(), xPos, y, zPos),
                                new Location(p.getWorld(), xPos, y + 1, zPos),
                                new Location(p.getWorld(), xPos, y + 2, zPos));
                    }
                } else {
                    for (double zPos = z2; zPos < z1 + 1; zPos++) {
                        setBedrock(new Location(p.getWorld(), xPos, y, zPos),
                                new Location(p.getWorld(), xPos, y + 1, zPos),
                                new Location(p.getWorld(), xPos, y + 2, zPos));
                    }
                }
            }
        } else {
            for (double xPos = x2; xPos < x1 + 1; xPos++) {
                if (z1 <= z2) {
                    for (double zPos = z1; zPos < z2 + 1; zPos++) {
                        setBedrock(new Location(p.getWorld(), xPos, y, zPos),
                                new Location(p.getWorld(), xPos, y + 1, zPos),
                                new Location(p.getWorld(), xPos, y + 2, zPos));
                    }
                } else {
                    for (double zPos = z2; zPos < z1 + 1; zPos++) {
                        setBedrock(new Location(p.getWorld(), xPos, y, zPos),
                                new Location(p.getWorld(), xPos, y + 1, zPos),
                                new Location(p.getWorld(), xPos, y + 2, zPos));
                    }
                }
            }
        }
    }

    private static void setBedrock(Location a, Location b, Location c) {
        a.getBlock().setType(Material.BEDROCK);
        b.getBlock().setType(Material.AIR);
        c.getBlock().setType(Material.AIR);
    }

    public static Location teleportTo(World w) {
        String environment;

        switch (w.getEnvironment()) {
            case NORMAL:
                environment = "overworld";
                break;

            case NETHER:
                environment = "nether";
                break;

            default:
                environment = "end";
                break;
        }

        double x1 = plugin.getConfig().getDouble(environment + ".x1");
        double x2 = plugin.getConfig().getDouble(environment + ".x2");
        double y = plugin.getConfig().getDouble(environment + ".y");
        double z1 = plugin.getConfig().getDouble(environment + ".z1");
        double z2 = plugin.getConfig().getDouble(environment + ".z2");

        return new Location(w, (x1 + x2)/2, y + 1, (z1 + z2)/2);
    }
}
