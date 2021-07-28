package me.awesomemoder316.passwordlogon;

import me.awesomemoder316.passwordlogon.commands.Password;
import me.awesomemoder316.passwordlogon.listeners.*;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;


public class PasswordLogOn extends JavaPlugin {

    public void onEnable() {

        Utils.plugin = this;

        getConfig().options().copyDefaults(true);
        getConfig().set("configVersion", "1.3.0");
        getConfig().options().copyHeader(true);
        saveConfig();

        Bukkit.getPluginManager().registerEvents(new OnPlayerChat(), this);
        Bukkit.getPluginManager().registerEvents(new OnPlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new OnPlayerLeave(), this);
        Bukkit.getPluginManager().registerEvents(new OnPlayerRespawn(), this);
        Bukkit.getPluginManager().registerEvents(new RestrictActions(), this);
        Objects.requireNonNull(getCommand("password")).setExecutor(new Password());
        Objects.requireNonNull(getCommand("password")).setTabCompleter(new TabComplete());

        for (Player p : Bukkit.getOnlinePlayers()) {
            Utils.noPasswordEntered.put(p.getUniqueId(), p.getLocation());
        }
        Utils.giveResistance();

        new Metrics(this, 11006);

        Utils.check();

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


}
