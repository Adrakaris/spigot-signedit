package com.bocbin.testplugin.components;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/*
This is a /doctor command which heals the person
 */
public class Doctor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equals("doctor")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                if (!player.hasPermission("testplugin.doctor")) {
                    player.sendMessage(ChatColor.RED + "You do not have the permission testplugin.doctor!");
                    return true;
                }

                if (args.length == 0) {
                    // clickable and hoverable events use bungee api, so one needs bungee chatcolor
                    TextComponent message = new TextComponent("Would you like some healing?");
                    message.setColor(net.md_5.bungee.api.ChatColor.GOLD);
                    message.setBold(true);
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/doctor healme"));
                    message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder("Click here to be healed, on the NHS")
                                    .color(net.md_5.bungee.api.ChatColor.GRAY).italic(true).create()));  // for second param, cannot use string!
                    // send to player a component
                    player.spigot().sendMessage(message);
                    return true;
                }

                if (args.length == 1 && args[0].equalsIgnoreCase("healme")) {
                    player.setHealth(Math.min(player.getHealth()+4, 20));
                    // to send a command through console
                    // Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "effect give " + player.getName() + "minecraft:regeneration 30");
                    player.sendMessage(ChatColor.GREEN + "You have been healed!");
                    return true;
                }

                player.sendMessage(ChatColor.RED + "usage: /doctor");

            } else {
                sender.sendMessage("A console cannot use heal");
            }
            return true;
        }

        return false;
    }
}
