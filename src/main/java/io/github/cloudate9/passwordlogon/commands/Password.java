package io.github.cloudate9.passwordlogon.commands;

import io.github.cloudate9.passwordlogon.MessageConfig;
import io.github.cloudate9.passwordlogon.Utils;
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
                    return true;
                }

                switch (args[0].toLowerCase()) {
                    case "set":

                        if (!(sender instanceof Player)) {
                            new MessageConfig().rejectConsole(sender);
                            return true;
                        }

                        Player p = (Player) sender;

                        if (Utils.plugin.getConfig().contains("password." + p.getUniqueId())) {
                            new MessageConfig().alreadySetPassword(sender);
                            return true;
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
                            return true;
                        }

                        p = (Player) sender;

                        if (!(Utils.plugin.getConfig().contains("password." + p.getUniqueId()))) {
                            new MessageConfig().noPasswordSet(sender);
                            return true;
                        }
                            if (args.length < 3) {
                                new MessageConfig().incorrectUsage(sender, "reset");
                                return true;
                            }
                                String s = Utils.plugin.getConfig().getString("password." + p.getUniqueId());
                                String oldPassword = args[1];
                                String newPassword = args[2];

                        assert s != null;
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

                        if (sender instanceof Player) {
                            new MessageConfig().rejectPlayer(sender);
                        }

                        if (args.length < 7) {
                            new MessageConfig().incorrectUsage(sender, "setArea");
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
                                new MessageConfig().incorrectUsage(sender, "setArea");
                                return true;
                        }

                        if (cannotParseDouble(args[2]) ||
                                cannotParseDouble(args[3]) ||
                                cannotParseDouble(args[4]) ||
                                cannotParseDouble(args[5]) ||
                                cannotParseDouble(args[6])
                        ) {
                            new MessageConfig().incorrectUsage(sender, "setAreaParameterType");
                            return true;
                        }
                        Utils.plugin.getConfig().set(dimension + ".x1", Double.parseDouble(args[2]));
                        Utils.plugin.getConfig().set(dimension + ".x2", Double.parseDouble(args[3]));
                        Utils.plugin.getConfig().set(dimension + ".y", Double.parseDouble(args[4]));
                        Utils.plugin.getConfig().set(dimension +".z1", Double.parseDouble(args[5]));
                        Utils.plugin.getConfig().set(dimension + ".z2", Double.parseDouble(args[6]));
                        Utils.plugin.getConfig().options().copyHeader(true);
                        Utils.plugin.saveConfig();

                        new MessageConfig().loginAreaChanged(sender);
                }
        }
        return true;

    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (args.length == 0) return null;

        if (args.length == 1)
            return StringUtil.copyPartialMatches(args[0], Arrays.asList("reset", "set", "setarea"), new ArrayList<>());


        switch (args[0].toLowerCase()) {

            case "reset":

                switch (args.length) {
                    //Not 1 as alr checked.

                    case 2:
                        return StringUtil.copyPartialMatches(args[1], Collections.singletonList("(old password)"), new ArrayList<>());

                    case 3:
                        return StringUtil.copyPartialMatches(args[2], Collections.singletonList("(new password)"), new ArrayList<>());

                    default:
                        return StringUtil.copyPartialMatches(args[3], Collections.singletonList(""), new ArrayList<>());
                }

            case "set":

                if (args.length == 2) {
                    return StringUtil.copyPartialMatches(args[1], Collections.singletonList("(password)"), new ArrayList<>());
                }
                return StringUtil.copyPartialMatches(args[2], Collections.singletonList(""), new ArrayList<>());

            case "setarea":

                switch (args.length) {

                    case 2:
                        return StringUtil.copyPartialMatches(args[1], Arrays.asList("end", "nether", "overworld"), new ArrayList<>());

                    case 3:
                        return StringUtil.copyPartialMatches(args[2], Collections.singletonList("(x1)"), new ArrayList<>());

                    case 4:
                        return StringUtil.copyPartialMatches(args[3], Collections.singletonList("(x2)"), new ArrayList<>());

                    case 5:
                        return StringUtil.copyPartialMatches(args[4], Collections.singletonList("(y)"), new ArrayList<>());

                    case 6:
                        return StringUtil.copyPartialMatches(args[5], Collections.singletonList("(z1)"), new ArrayList<>());

                    case 7:
                        return StringUtil.copyPartialMatches(args[6], Collections.singletonList("(z2)"), new ArrayList<>());

                    default:
                        return StringUtil.copyPartialMatches(args[7], Collections.singletonList(""), new ArrayList<>());

                }

            default:
                return StringUtil.copyPartialMatches(args[1], Collections.singletonList(""), new ArrayList<>());

        }
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
