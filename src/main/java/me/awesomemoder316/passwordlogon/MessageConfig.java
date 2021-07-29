package me.awesomemoder316.passwordlogon;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

@SuppressWarnings("ConstantConditions")
public class MessageConfig {

    private final YamlConfiguration config;

    public MessageConfig() {
        File messageYml = new File(Utils.plugin.getDataFolder(), "messages.yml");

        if (!messageYml.exists()) Utils.plugin.saveResource("messages.yml", false);

        config = YamlConfiguration.loadConfiguration(messageYml);
    }

    public void alreadySetPassword(CommandSender sender) {
        sender.sendMessage(
                ChatColor.translateAlternateColorCodes(
                        '&', config.getString("errors.alreadySetPassword")
                .replace("\\n", "\n")));
    }

    public void failedLogIn(CommandSender sender) {
        sender.sendMessage(
                ChatColor.translateAlternateColorCodes(
                        '&', config.getString("errors.failedLogIn")));
    }

    public void failedReset(CommandSender sender) {
        sender.sendMessage(
                ChatColor.translateAlternateColorCodes(
                        '&', config.getString("errors.failedReset")));
    }

    public void incorrectUsage(CommandSender sender, String type) {

        String path;

        switch (type.toLowerCase()) {
            case "generic":
                path = "errors.incorrectUsage.generic";
                break;

            case "reset":
                path = "errors.incorrectUsage.reset";
                break;

            case "setArea":
                path = "errors.incorrectUsage.setArea";
                break;

            default: //case "setAreaParameterType":
                path = "errors.incorrectUsage.setAreaParameterType";
                break;
        }

        sender.sendMessage(
                ChatColor.translateAlternateColorCodes(
                        '&', config.getString(path)));
    }

    public void loginPrompt(CommandSender sender) {
        sender.sendMessage(
                ChatColor.translateAlternateColorCodes(
                        '&', config.getString("loginPrompt")));
    }

    public void noPasswordSet(CommandSender sender) {
        sender.sendMessage(
                ChatColor.translateAlternateColorCodes(
                        '&', config.getString("errors.noPasswordSet")));
    }

    public void passwordSetConfirmation(CommandSender sender, String playerPassword) {
        sender.sendMessage(
                ChatColor.translateAlternateColorCodes(
                        '&', config.getString("success.passwordSetConfirmation")
                                .replace("$playerPassword", playerPassword)));
    }

    public void passwordCreatePrompt(CommandSender sender) {
        sender.sendMessage(
                ChatColor.translateAlternateColorCodes(
                        '&', config.getString("passwordCreatePrompt")));
    }

    public void rejectConsole(CommandSender sender) {
        sender.sendMessage(
                ChatColor.translateAlternateColorCodes(
                        '&', config.getString("errors.rejectConsole")));
    }

    public void rejectPlayer(CommandSender sender) {
        sender.sendMessage(
                ChatColor.translateAlternateColorCodes(
                        '&', config.getString("errors.rejectPlayer")));
    }

    public void restrictAction(CommandSender sender) {
        sender.sendMessage(
                ChatColor.translateAlternateColorCodes(
                        '&', config.getString("errors.restrictAction")));
    }

    public void successfulLogIn(CommandSender sender) {
        sender.sendMessage(
                ChatColor.translateAlternateColorCodes(
                        '&', config.getString("success.successfulLogIn")));
    }

}
