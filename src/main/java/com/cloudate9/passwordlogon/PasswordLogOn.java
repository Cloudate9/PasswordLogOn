package com.cloudate9.passwordlogon;

import com.cloudate9.passwordlogon.auth.PlayerStatus;
import com.cloudate9.passwordlogon.listeners.NoAuthLogout;
import com.cloudate9.passwordlogon.listeners.PrepareForAuth;
import com.cloudate9.passwordlogon.listeners.RestrictActions;
import com.cloudate9.passwordlogon.messages.MessageConfig;
import com.cloudate9.passwordlogon.passwords.PlayerPassword;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PasswordLogOn extends JavaPlugin {

    private final MessageConfig messageConfig = new MessageConfig(this);
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final PlayerPassword playerPassword = new PlayerPassword(this);


    @Override
    public void onEnable() {
//        migrateConfig();
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new NoAuthLogout(), this);
        Bukkit.getPluginManager().registerEvents(new PrepareForAuth(this), this);
        Bukkit.getPluginManager().registerEvents(new RestrictActions(messageConfig, miniMessage), this);

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            if (!getConfig().getBoolean("no_auth_should_glow")) return;
            for (String playerName : PlayerStatus.getNonAuthPlayers().keySet()) {
                Player p = Bukkit.getPlayer(playerName);
                if (p == null) continue;
                // Remember to remove potion effect from all players who auth, regardless of config option!
                p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 120, 1, false, false, false));
            }
        }, 0L, 100L);
    }

    private void migrateConfig() {
        // Ignore this for now. This is not mapping to the right things anyway
        FileConfiguration config = getConfig();
        config.set("location.overworld.x", (getConfig().getDouble("overworld.x1") + getConfig().getDouble("overworld.x2")) / 2);
        config.set("location.overworld.y", getConfig().getDouble("overworld.y"));
        config.set("location.overworld.z", (getConfig().getDouble("overworld.z1") + getConfig().getDouble("overworld.z2")) / 2);
        config.set("overworld", null);

        config.set("location.nether.x", (getConfig().getDouble("nether.x1") + getConfig().getDouble("nether.x2")) / 2);
        config.set("location.nether.y", getConfig().getDouble("nether.y"));
        config.set("location.nether.z", (getConfig().getDouble("nether.z1") + getConfig().getDouble("nether.z2")) / 2);
        config.set("nether", null);

        config.set("location.end.x", (getConfig().getDouble("end.x1") + getConfig().getDouble("end.x2")) / 2);
        config.set("location.end.y", getConfig().getDouble("end.y"));
        config.set("location.end.z", (getConfig().getDouble("end.z1") + getConfig().getDouble("end.z2")) / 2);
        config.set("end", null);
    }

}
