package io.github.cloudate9.passwordlogon;

import io.github.cloudate9.passwordlogon.commands.Password;
import io.github.cloudate9.passwordlogon.listeners.*;
import kr.entree.spigradle.annotations.SpigotPlugin;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.*;
import java.net.URL;
import java.util.*;

@SpigotPlugin
public class PasswordLogOn extends JavaPlugin {

    private boolean firstCheck = true;

    public void onEnable() {

        Utils.plugin = this;

        getConfig().options().copyDefaults(true);
        getConfig().set("configVersion", null); //Revert a change from earlier versions.
        getConfig().options().copyHeader(true);
        saveConfig();

        new MessageConfig();

        Bukkit.getPluginManager().registerEvents(new OnPlayerChat(), this);
        Bukkit.getPluginManager().registerEvents(new OnPlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new OnPlayerLeave(), this);
        Bukkit.getPluginManager().registerEvents(new OnPlayerRespawn(), this);
        Bukkit.getPluginManager().registerEvents(new RestrictActions(), this);
        Objects.requireNonNull(getCommand("password")).setExecutor(new Password());

        for (Player p : Bukkit.getOnlinePlayers()) {
            Utils.noPasswordEntered.put(p.getUniqueId(), p.getLocation());
        }
        giveResistance();

        new Metrics(this, 11006);

        check(this);

        //Transfer legacy settings. Check file manually instead of config cause getDouble() returns 0 if null.
        List<String> oldLocSettings = Arrays.asList("x1", "x2", "y", "z1", "z2");
        try {

            for (String setting: oldLocSettings) {

                File file = new File(getDataFolder() + File.separator + "config.yml");
                Scanner scanner = new Scanner(file);

                while (scanner.hasNextLine()) {

                    String line = scanner.nextLine();
                    if (line.startsWith(setting)) {
                        double value = Double.parseDouble(line.replace(setting + ": ", ""));
                        getConfig().set("overworld." + setting, value);
                        getConfig().set("nether." + setting, value);
                        getConfig().set("end." + setting, value);
                        getConfig().set(setting, null);
                        getConfig().options().copyDefaults(true);
                        saveConfig();
                    }
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void onDisable() {
        getConfig().options().copyHeader(true);
        saveConfig();
    }

    private void giveResistance() {
        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(Utils.plugin, () -> {
            for (UUID u : Utils.noPasswordEntered.keySet()) {
                Player p = Bukkit.getPlayer(u);

                if (p != null) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 61, 254));
                }
            }
        }, 0, 60);
    }

    private void check(JavaPlugin plugin) {

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Scanner readGit = new Scanner(
                            new InputStreamReader(
                                    new URL(
                                            "https://raw.githubusercontent.com/Cloudate9/PasswordLogOn/master/build.gradle.kts"
                                    ).openStream()
                            )
                    );

                    var version = "";
                    while (readGit.hasNext()) {
                        String line = readGit.nextLine();

                        if (line.startsWith("version = ")) {
                            //Targeted line example: version = "2.0.0"
                            //Is removal of suffix '"' required?
                            version = line.split("\"")[1];
                            break;
                        }
                    }

                    if (Objects.equals(version, plugin.getDescription().getVersion())) {
                        if (firstCheck) {
                            //No parsing, cause a String is required.
                            plugin.getLogger().info(ChatColor.AQUA + "is up to date!");
                            firstCheck = false;
                        }

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                check(plugin);
                            }
                            //Arbitrary delay, to reduce unnecessary checks.
                        }.runTaskLaterAsynchronously(plugin, 576000);

                        return;
                    }

                    Utils.update = true;
                    getLogger().info(ChatColor.AQUA + "can be updated at https://www.curseforge.com/minecraft/bukkit-plugins/password-log-on!");

                } catch (IOException ex) {
                    getLogger().info( ChatColor.RED + "Failed to check for updates!");

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            check(plugin);
                        }
                        //Arbitrary delay, to reduce unnecessary checks.
                    }.runTaskLaterAsynchronously(plugin, 576000);
                }
            }
        }.runTaskAsynchronously(this);
    }


}
