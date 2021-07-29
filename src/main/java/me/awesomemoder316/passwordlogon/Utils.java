package me.awesomemoder316.passwordlogon;

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
import java.util.Scanner;
import java.util.UUID;

public class Utils {
    private static boolean firstCheck = true;
    public static HashMap<UUID, Location> noPasswordEntered = new HashMap<>();
    public static boolean update = false;
    public static PasswordLogOn plugin;

    public static void askPassword(Player p) {
        Utils.defineBedrockPos(p);
        Utils.noPasswordEntered.put(p.getUniqueId(), p.getLocation());

        p.teleport(teleportTo(p.getWorld()));

        if (plugin.getConfig().contains("password." + p.getUniqueId())) {
            p.sendMessage(ChatColor.GOLD + "[Password Log On]: Enter your password in chat to continue playing.");
            p.sendMessage(ChatColor.AQUA + "If you forgot your password, contact someone with access to the files of the server.");
        } else {
            p.sendMessage(ChatColor.YELLOW + "[Password Log On] Set a password to continue playing, using /password set [(password)]");
        }
    }

    public static void check() { //The proper method can be found in "ModersLib" by Awesomemoder316
        new BukkitRunnable() {
            @Override
            public void run() {
                try {

                    BufferedReader read = new BufferedReader(
                            new InputStreamReader(
                                    new URL(
                                            "https://api.github.com/repos/awesomemoder316/PasswordLogOn/releases"
                                    ).openStream()
                            ));

                    StringBuilder result = new StringBuilder();

                    while (true) {
                        String output = read.readLine();

                        if (output == null) break;
                        else result.append("\n").append(output);
                    }

                    read.close();

                    String[] jsonList = result.toString().split(",");

                    boolean isLatestVersion = true;
                    String latestTag;

                    for (String part : jsonList) {

                        if (part.startsWith("\"tag_name\":\"")) {

                            latestTag = part
                                    .replaceFirst("^\"tag_name\":\"", "");

                            if (latestTag.endsWith("\"")) latestTag = part.substring(0, part.length() - 1);


                            if (latestTag.equals(plugin.getDescription().getVersion())) {

                                if (isLatestVersion) {

                                    if (firstCheck) {
                                        plugin.getLogger().info(ChatColor.AQUA + "is up to date!");
                                        firstCheck = false;
                                    }

                                    Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, Utils::check, 576000);
                                    return;
                                }

                                break;
                            }

                            if (latestTag.toLowerCase().contains("alpha") || latestTag.toLowerCase().contains("beta"))
                                continue;

                            isLatestVersion = false;

                            update = true;
                            plugin.getLogger().info(ChatColor.AQUA + "can be updated at https://www.curseforge.com/minecraft/bukkit-plugins/password-log-on!");

                        }
                    }

                } catch (IOException ex) {
                    plugin.getLogger().info( ChatColor.RED + "Failed to check for updates!");

                    Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, Utils::check, 576000);

                }
            }
        }.runTaskAsynchronously(plugin);
    }

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

    public static void giveResistance() {
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(plugin, () -> {
            for (UUID u : noPasswordEntered.keySet()) {
                Bukkit.getPlayer(u).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 61, 254));
            }
        }, 0, 60);
    }

    private static void setBedrock(Location a, Location b, Location c) {
        a.getBlock().setType(Material.BEDROCK);
        b.getBlock().setType(Material.AIR);
        c.getBlock().setType(Material.AIR);
    }

    public static void teleportBack(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                player.teleport(noPasswordEntered.get(player.getUniqueId()));
                player.playSound(player.getLocation(), Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1, 1);
                noPasswordEntered.remove(player.getUniqueId());
            }
        }.runTaskLater(plugin, 120L);
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
                }.runTaskLater(plugin, 120L);
            }
        }.runTaskLater(plugin, 0L);
    }
}
