package me.awesomemoder316.passwordlogon;

import me.awesomemoder316.passwordlogon.commands.Password;
import me.awesomemoder316.passwordlogon.listeners.*;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Objects;


public class PasswordLogOn extends JavaPlugin {

    public void onEnable() {

        Utils.plugin = this;

        getConfig().options().copyDefaults(true);
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
    }

    public void onDisable() {
        getConfig().options().copyHeader(true);
        saveConfig();
    }


}
