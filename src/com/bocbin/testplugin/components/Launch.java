package com.bocbin.testplugin.components;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Hashtable;

import static org.bukkit.Bukkit.getServer;

public class Launch implements CommandExecutor {
    // comandexectutors let commands be store in other classes
    static Hashtable<Integer, String> colourTable = createColourTable();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (label.equals("launch") || label.equals("lh")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                if (player.hasPermission("testplugin.launch")) {// have /launch. or /launch [vel]  --  use the args parameter
                    if (args.length == 0) {
                        // do basic
                        player.sendMessage(ChatColor.BOLD + "Zooom!");
                        player.setVelocity(player.getLocation().getDirection().multiply(2).setY(2));

                    } else if (args.length == 1) {
                        // require 2 digit number
                        if (args[0].length() > 2) {
                            player.sendMessage(ChatColor.RED + "<velocity> must be under 100!");
                            return true;
                        }

                        // colour code message
                        int velocity;
                        try {
                            velocity = Integer.parseInt(args[0]);
                        } catch (NumberFormatException e) {  // catches in the case a non integer number is entered
                            player.sendMessage(ChatColor.RED + "<velocity> must be an integer!");
                            return true;
                        }
                        // some colour coding logic
                        int velocityProportion = Math.min(Math.floorDiv(velocity, 5), 7);  // divides input velocity by 5
                        String textColour = colourTable.get(velocityProportion);  // provides a colour code
                        // player.sendMessage("<DBG> Textcolour is " + textColour + " VelocityProportion is " + velocityProportion);

                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&" + textColour + "&lZooom!"));
                        player.setVelocity(player.getLocation().getDirection().multiply(velocity).setY(velocity));

                    } else if (args.length == 2) {
                        if (player.hasPermission("testplugin.launch.others")) {
                            return LaunchOnOthers(player, args);

                        } else {
                            player.sendMessage(ChatColor.RED + "You do not have the permission testplugin.launch.others!\nUsage: /launch [velocity]");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Usage: /launch [velocity] [player]");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "You do not have the permission testplugin.launch!");
                }
                return true;

            } else {
                // console
                if (args.length == 2) {
                    return LaunchOnOthers(sender, args);
                } else {
                    sender.sendMessage(ChatColor.RED + "Usage: /launch <velocity> <player>");
                }
            }
            return true;
        }

        return false;
    }

    private boolean LaunchOnOthers(CommandSender sender, String[] args) {
        Player target = getServer().getPlayer(args[1]);
        if (target != null) {
            int velocity;
            try {
                velocity = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {  // catches in the case a non integer number is entered
                sender.sendMessage(ChatColor.RED + "<velocity> must be an integer!");
                return true;
            }

            // some colour coding logic
            int velocityProportion = Math.min(Math.floorDiv(velocity, 5), 7);  // divides input velocity by 5
            String textColour = colourTable.get(velocityProportion);  // provides a colour code
            // player.sendMessage("<DBG> Textcolour is " + textColour + " VelocityProportion is " + velocityProportion);

            target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&" + textColour + "&lZooom!"));
            target.setVelocity(target.getLocation().getDirection().multiply(velocity).setY(velocity));
        } else {
            sender.sendMessage(ChatColor.RED + "This player cannot be found");
        }

        return true;
    }

    private static Hashtable<Integer, String> createColourTable() {
        Hashtable<Integer, String> out = new Hashtable<>();
        out.put(0, "4");  // red
        out.put(1, "6");  // orange
        out.put(2, "e");  // yellow
        out.put(3, "a");  // green
        out.put(4, "b");  // blue
        out.put(5, "1");  // indigo
        out.put(6, "5");  // violet
        out.put(7, "0");  // black

        return out;
    }
}
