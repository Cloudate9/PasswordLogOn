package me.awesomemoder316.passwordlogon;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TabComplete implements TabCompleter {
    private static final List<String> COMMANDS = Arrays.asList("set", "reset");
    private static final List<String> COMMANDS2 = Collections.singletonList("(password)");
    private static final List<String> COMMANDS3 = Collections.singletonList("(old password)");
    private static final List<String> COMMANDS4 = Collections.singletonList("(new password)");
    private static final List<String> BLANK = Arrays.asList("", "");

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length > 3) {
            return StringUtil.copyPartialMatches(args[3], BLANK, new ArrayList<>());
        } else if (args.length > 2 && args[0].equalsIgnoreCase("reset")) {
            return StringUtil.copyPartialMatches(args[2], COMMANDS4, new ArrayList<>());
        } else if (args.length > 1) {
            if (args[0].equalsIgnoreCase("reset")) {
                return StringUtil.copyPartialMatches(args[1], COMMANDS3, new ArrayList<>());
            } else if (args[0].equalsIgnoreCase("set")) {
                return StringUtil.copyPartialMatches(args[1], COMMANDS2, new ArrayList<>());
            }
        }
        return (args.length == 1) ? StringUtil.copyPartialMatches(args[0], COMMANDS, new ArrayList<>()) : null;
    }
}
