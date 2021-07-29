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
                                .replace("\\n", "\n")
                                .replace("$pluginPrefix", config.getString("pluginPrefix"))
                ));
    }

    public void failedLogIn(CommandSender sender) {
        sender.sendMessage(
                ChatColor.translateAlternateColorCodes(
                        '&', config.getString("errors.failedLogIn")
                                .replace("$pluginPrefix", config.getString("pluginPrefix"))
                ));
    }

    public void failedReset(CommandSender sender) {
        sender.sendMessage(
                ChatColor.translateAlternateColorCodes(
                        '&', config.getString("errors.failedReset")
                                .replace("$pluginPrefix", config.getString("pluginPrefix"))
                ));
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
                        '&', config.getString(path)
                                .replace("$pluginPrefix", config.getString("pluginPrefix"))
                ));
    }

    public void loginPrompt(CommandSender sender) {
        sender.sendMessage(
                ChatColor.translateAlternateColorCodes(
                        '&', config.getString("loginPrompt")
                                .replace("$pluginPrefix", config.getString("pluginPrefix"))
                ));
    }

    public void noPasswordSet(CommandSender sender) {
        sender.sendMessage(
                ChatColor.translateAlternateColorCodes(
                        '&', config.getString("errors.noPasswordSet")
                                .replace("$pluginPrefix", config.getString("pluginPrefix"))
                ));
    }

    public void passwordSetConfirmation(CommandSender sender, String playerPassword) {
        sender.sendMessage(
                ChatColor.translateAlternateColorCodes(
                        '&', config.getString("success.passwordSetConfirmation")
                                .replace("$playerPassword", playerPassword)
                                .replace("$pluginPrefix", config.getString("pluginPrefix"))
                ));
    }

    public void passwordCreatePrompt(CommandSender sender) {
        sender.sendMessage(
                ChatColor.translateAlternateColorCodes(
                        '&', config.getString("passwordCreatePrompt")
                                .replace("$pluginPrefix", config.getString("pluginPrefix"))
                ));
    }

    public void rejectConsole(CommandSender sender) {
        sender.sendMessage(
                ChatColor.translateAlternateColorCodes(
                        '&', config.getString("errors.rejectConsole")
                                .replace("$pluginPrefix", config.getString("pluginPrefix"))
                ));
    }

    public void rejectPlayer(CommandSender sender) {
        sender.sendMessage(
                ChatColor.translateAlternateColorCodes(
                        '&', config.getString("errors.rejectPlayer")
                                .replace("$pluginPrefix", config.getString("pluginPrefix"))
                ));
    }

    public void restrictAction(CommandSender sender) {
        sender.sendMessage(
                ChatColor.translateAlternateColorCodes(
                        '&', config.getString("errors.restrictAction")
                                .replace("$pluginPrefix", config.getString("pluginPrefix"))
                                ));
    }

    public void successfulLogIn(CommandSender sender) {
        sender.sendMessage(
                ChatColor.translateAlternateColorCodes(
                        '&', config.getString("success.successfulLogIn")
                .replace("$pluginPrefix", config.getString("pluginPrefix"))
                ));
    }

}
