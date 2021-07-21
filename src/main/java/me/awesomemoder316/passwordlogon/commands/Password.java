package me.awesomemoder316.passwordlogon.commands;

import me.awesomemoder316.passwordlogon.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Password implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("password")) {

                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Incorrect usage: command is /password [set/reset] [(password)]");
                    return false;
                }
                switch (args[0].toLowerCase()) {
                    case "set":

                        if (!(sender instanceof Player)) {
                            sender.sendMessage(ChatColor.RED + "The console does not need a password!");
                            sender.sendMessage(ChatColor.GOLD + "[Password log on]: To reset a player's password, delete the player's UUID and password in this plugin's config.yml");
                            return false;
                        }

                        Player p = (Player) sender;

                        if (Utils.plugin.getConfig().contains("password." + p.getUniqueId())) {
                            sender.sendMessage(ChatColor.RED + "You have set a password in the past! Use /password reset [(old password)] [(new password)] to change!");
                            sender.sendMessage(ChatColor.RED + "If you believe that someone else not authorised by you set the password, contact your console operator!");
                            return false;
                        }

                            String playerPassword = args[1];
                            Utils.plugin.getConfig().set("password." + p.getUniqueId(), playerPassword);
                            Utils.plugin.getConfig().options().copyHeader(true);
                            Utils.plugin.saveConfig();
                            sender.sendMessage(ChatColor.GOLD + "You set your password as " + playerPassword + " ! You will need to use this password everytime you log on.");

                    return true;

                    case "reset":

                        if (!(sender instanceof Player)) {
                            sender.sendMessage(ChatColor.RED + "The console does not need a password!");
                            sender.sendMessage(ChatColor.GOLD + "[Password log on]: To reset a player's password, delete the player's UUID and password in this plugin's config.yml");
                            return false;
                        }

                        p = (Player) sender;

                        if (!(Utils.plugin.getConfig().contains("password." + p.getUniqueId()))) {
                            sender.sendMessage(ChatColor.RED + "You cannot reset your password as you do not have a current password stored.");
                            sender.sendMessage(ChatColor.RED + "Set your password instead, with /password set [(password)]");
                            return false;
                        }
                            if (args.length < 3) {
                                sender.sendMessage(ChatColor.RED + "Incorrect usage! Command is /password reset [(old password)] [(new password)]!");
                                return false;
                            }
                                String s = Utils.plugin.getConfig().getString("password." + p.getUniqueId());
                                String oldPassword = args[1];
                                String newPassword = args[2];

                                if (s.equals(oldPassword)) {
                                    Utils.plugin.getConfig().set("password." + p.getUniqueId(), newPassword);
                                    Utils.plugin.getConfig().options().copyHeader(true);
                                    Utils.plugin.saveConfig();
                                    sender.sendMessage(ChatColor.GOLD + "You reset your password to " + newPassword + " ! You will need to use this password everytime you log on.");
                                } else {
                                    sender.sendMessage(ChatColor.RED + "Your old password entered does not match that on record!");
                                }

                        return true;

                    case "setarea":

                        if (!(sender instanceof Player)) {
                            sender.sendMessage(ChatColor.RED + "Only the console can set the login area!");
                        }

                        if (args.length < 6) {
                            sender.sendMessage(ChatColor.RED + "Incorrect usage: command is /password setarea (x1) (x2) (y) (z1) (z2)");
                            sender.sendMessage(ChatColor.YELLOW + "The area between x1 and x2 is the x position of the platform (inclusive of x1 and x2)\n" +
                                    "y is the level at which the platform is at. y, and 2 blocks above y, will be cleared in order to stop the player from suffocating.\n" +
                                    "The area between z1 and z2 is the z position of the platform (inclusive of z1 and z2)");
                            return false;
                        }
                        if (cannotParseDouble(args[1]) ||
                                cannotParseDouble(args[2]) ||
                                cannotParseDouble(args[3]) ||
                                cannotParseDouble(args[4]) ||
                                cannotParseDouble(args[5])
                        ) {
                            sender.sendMessage(ChatColor.RED + "Incorrect usage: x1, x2, y, z1, and z2 all have to be numbers!");
                            return false;
                        }
                        Utils.plugin.getConfig().set("x1", Double.parseDouble(args[1]));
                        Utils.plugin.getConfig().set("x2", Double.parseDouble(args[2]));
                        Utils.plugin.getConfig().set("y", Double.parseDouble(args[3]));
                        Utils.plugin.getConfig().set("z1", Double.parseDouble(args[4]));
                        Utils.plugin.getConfig().set("z2", Double.parseDouble(args[5]));
                        Utils.plugin.getConfig().options().copyHeader(true);
                        Utils.plugin.saveConfig();
                }
        }
        return true;

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
