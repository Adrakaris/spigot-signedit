package com.bocbin.signeditor;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.Hash;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Hashtable;

public class Main extends JavaPlugin {

    static Hashtable<Integer, String> colourTable = createColourTable();

    @Override
    public void onEnable() {
        // runs on server startup, on reload, etc.
    }

    @Override
    public void onDisable() {
        // runs on shutdown

    }

    // on command function
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // command to /hello <- hello world
        if (label.equals("hello")) {  // can also use equalsIgnoreCase but boo
            if (sender instanceof Player) {  // a sender is NOT always a player
                Player player = (Player) sender;  // case sender to PLayer object
                if (player.hasPermission("signedit.hello")) {
                    player.sendMessage(ChatColor.GOLD + "" + ChatColor.ITALIC + "Hello World!");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&1r&2a&3i&4n&5b&6o&7w&8s&9!"));
                } else {
                    player.sendMessage(ChatColor.RED + "You do not have the permission signedit.hello!");
                }
            } else {
                sender.sendMessage("Hello Console!");
            }
            return true;
        }

        // command to launch very far
        if (label.equals("launch") || label.equals("lh")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                if (player.hasPermission("signedit.launch")) {// have /launch. or /launch [vel]  --  use the args parameter
                    if (args.length == 0) {
                        // do basic
                        player.sendMessage(ChatColor.BOLD + "Zooom!");
                        player.setVelocity(player.getLocation().getDirection().multiply(2).setY(2));

                    } else if (args.length == 1) {
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
                        if (player.hasPermission("signedit.launch.others")) {
                            Player target = getServer().getPlayer(args[1]);
                            if (target != null) {
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

                                target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&" + textColour + "&lZooom!"));
                                target.setVelocity(player.getLocation().getDirection().multiply(velocity).setY(velocity));
                            } else { player.sendMessage(ChatColor.RED + "This player cannot be found"); }


                        } else {
                            player.sendMessage(ChatColor.RED + "You do not have the permission signedit.launch.others!\nUsage: /launch [velocity]");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Usage: /launch [velocity] [player]");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "You do not have the permission signedit.launch!");
                }
                return true;

            } else {
                // console
                sender.sendMessage("Cannot be used by console");
            }
            return true;
        } else

        return false;
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
