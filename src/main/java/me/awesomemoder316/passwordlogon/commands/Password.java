package me.awesomemoder316.passwordlogon.commands;

import me.awesomemoder316.passwordlogon.MessageConfig;
import me.awesomemoder316.passwordlogon.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Password implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("password")) {

                if (args.length < 2) {
                    new MessageConfig().incorrectUsage(sender, "generic");
                    return false;
                }

                switch (args[0].toLowerCase()) {
                    case "set":

                        if (!(sender instanceof Player)) {
                            new MessageConfig().rejectConsole(sender);
                            return false;
                        }

                        Player p = (Player) sender;

                        if (Utils.plugin.getConfig().contains("password." + p.getUniqueId())) {
                            new MessageConfig().alreadySetPassword(sender);
                            return false;
                        }

                            String playerPassword = args[1];
                            Utils.plugin.getConfig().set("password." + p.getUniqueId(), playerPassword);
                            Utils.plugin.getConfig().options().copyHeader(true);
                            Utils.plugin.saveConfig();
                            new MessageConfig().passwordSetConfirmation(sender, playerPassword);

                    return true;

                    case "reset":

                        if (!(sender instanceof Player)) {
                            new MessageConfig().rejectConsole(sender);
                            return false;
                        }

                        p = (Player) sender;

                        if (!(Utils.plugin.getConfig().contains("password." + p.getUniqueId()))) {
                            new MessageConfig().noPasswordSet(sender);
                            return false;
                        }
                            if (args.length < 3) {
                                new MessageConfig().incorrectUsage(sender, "reset");
                                return false;
                            }
                                String s = Utils.plugin.getConfig().getString("password." + p.getUniqueId());
                                String oldPassword = args[1];
                                String newPassword = args[2];

                                if (s.equals(oldPassword)) {
                                    Utils.plugin.getConfig().set("password." + p.getUniqueId(), newPassword);
                                    Utils.plugin.getConfig().options().copyHeader(true);
                                    Utils.plugin.saveConfig();
                                    new MessageConfig().passwordSetConfirmation(sender, newPassword);
                                } else {
                                    new MessageConfig().failedReset(sender);
                                }

                        return true;

                    case "setarea":

                        if (!(sender instanceof Player)) {
                            new MessageConfig().rejectPlayer(sender);
                        }

                        if (args.length < 7) {
                            new MessageConfig().passwordSetConfirmation(sender, "setArea");
                            return true;
                        }

                        String dimension;
                        switch (args[1].toLowerCase()) {

                            case "overworld":
                            case "nether":
                            case "end":
                                dimension = args[1].toLowerCase();
                                break;

                            default:
                                new MessageConfig().passwordSetConfirmation(sender, "setArea");
                                return false;
                        }

                        if (cannotParseDouble(args[2]) ||
                                cannotParseDouble(args[3]) ||
                                cannotParseDouble(args[4]) ||
                                cannotParseDouble(args[5]) ||
                                cannotParseDouble(args[6])
                        ) {
                            new MessageConfig().passwordSetConfirmation(sender, "setAreaParameterType");
                            return true;
                        }
                        Utils.plugin.getConfig().set(dimension + ".x1", Double.parseDouble(args[1]));
                        Utils.plugin.getConfig().set(dimension + ".x2", Double.parseDouble(args[2]));
                        Utils.plugin.getConfig().set(dimension + ".y", Double.parseDouble(args[3]));
                        Utils.plugin.getConfig().set(dimension +".z1", Double.parseDouble(args[4]));
                        Utils.plugin.getConfig().set(dimension + ".z2", Double.parseDouble(args[5]));
                        Utils.plugin.getConfig().options().copyHeader(true);
                        Utils.plugin.saveConfig();
                }
        }
        return true;

    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (args.length > 3) {
            return StringUtil.copyPartialMatches(args[3], Collections.singletonList(""), new ArrayList<>());

        } else if (args.length > 2 && args[0].equalsIgnoreCase("reset")) {

            return StringUtil.copyPartialMatches(args[2], Collections.singletonList("(new password)"), new ArrayList<>());

        } else if (args.length > 1) {

            if (args[0].equalsIgnoreCase("reset")) {

                return StringUtil.copyPartialMatches(args[1], Collections.singletonList("(old password)"), new ArrayList<>());

            } else if (args[0].equalsIgnoreCase("set")) {

                return StringUtil.copyPartialMatches(args[1], Collections.singletonList("(password)"), new ArrayList<>());
            }
        }
        return (args.length == 1) ? StringUtil.copyPartialMatches(args[0], Arrays.asList("reset", "set"), new ArrayList<>()) : null;
    }

    private boolean cannotParseDouble(String arg) {
        try {
            Double.parseDouble(arg);
            return false;
        } catch (NumberFormatException ex) {
            return true;
        }
    }

}
