package com.bocbin.testplugin.components;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChangeTeam implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equals("changeteam") || label.equals("changeteams")) {
            if (sender instanceof Player) {

            } else {
                sender.sendMessage("");
            }
        }

        return false;
    }
}
