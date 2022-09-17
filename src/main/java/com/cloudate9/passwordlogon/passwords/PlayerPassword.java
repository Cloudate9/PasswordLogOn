package com.cloudate9.passwordlogon.passwords;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class PlayerPassword {

    private final Plugin plugin;
    private final Path passwordPath;
    private YamlConfiguration passwords;

    public PlayerPassword(Plugin plugin) throws IOException {
        this.plugin = plugin;
        passwordPath = Path.of(plugin.getDataFolder().getAbsolutePath(), "passwords.yml");
        if (!Files.exists(passwordPath)) Files.createFile(passwordPath);
        passwords = YamlConfiguration.loadConfiguration(passwordPath.toFile());
    }

    public String getPasswordPBKDF2HashString(String username) {
        if (passwords == null) return null;
        List<?> storedPasswords = passwords.getList("");
        if (storedPasswords == null) return null;

        Iterator<?> passwordsIter = storedPasswords.iterator();
        while (passwordsIter.hasNext()) {
            Object potentialPassword = passwordsIter.next();
            try {
                if (Objects.equals(((PasswordSchema) potentialPassword).username(), username)) {
                    passwords.set("", storedPasswords);
                    passwords.save(passwordPath.toFile());
                    return ((PasswordSchema) potentialPassword).passwordHash();
                }
            } catch (ClassCastException ex) {
                plugin.getLogger().warning("Removing invalid password entry of: " + potentialPassword);
                passwordsIter.remove();

            } catch (IOException ex) {
                /*
                It's not the end of the world if cleaning up of passwords fail.
                Continue as per usual, as the primary purpose of this function isn't to clean up the config anyway
                 */
                plugin.getLogger().warning("Removal failed for password entry: " + potentialPassword);
                ex.printStackTrace();
            }
        }

        return null;
    }

    public void addPassword(String username, String passwordHash) throws IOException {
        List<?> oldPasswords = passwords.getList("");

        List<PasswordSchema> updatedPasswords = new ArrayList<>();

        if (oldPasswords != null) {
            for (Object potentialPassword : oldPasswords) {
                try {
                    updatedPasswords.add((PasswordSchema) potentialPassword);
                } catch (ClassCastException ex) {
                    plugin.getLogger().warning("Removing invalid password entry of: " + potentialPassword);
                }
            }
        }

        updatedPasswords.add(new PasswordSchema(username, passwordHash));
        passwords.set("", updatedPasswords);
        passwords.save(passwordPath.toFile());
    }

    public void reload() {
        passwords = YamlConfiguration.loadConfiguration(passwordPath.toFile());
    }

}
