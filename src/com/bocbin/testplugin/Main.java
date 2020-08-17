package com.bocbin.testplugin;

import com.bocbin.testplugin.commands.Doctor;
import com.bocbin.testplugin.commands.Launch;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // runs on server startup, on reload, etc.
        // this gets some commands from the commands package
        this.getCommand("launch").setExecutor(new Launch());
        this.getCommand("doctor").setExecutor(new Doctor());
    }

    @Override
    public void onDisable() {
        // runs on shutdown

    }

    // on command function
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // this is if one wants to put everything in main, which is bad
        // command to /hello <- hello world
        if (label.equals("hello")) {  // can also use equalsIgnoreCase but boo
            if (sender instanceof Player) {  // a sender is NOT always a player
                Player player = (Player) sender;  // case sender to PLayer object
                if (player.hasPermission("testplugin.hello")) {
                    player.sendMessage(ChatColor.GOLD + "" + ChatColor.ITALIC + "Hello World!");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&1r&2a&3i&4n&5b&6o&7w&8s&9!"));
                } else {
                    player.sendMessage(ChatColor.RED + "You do not have the permission testplugin.hello!");
                }
            } else {
                sender.sendMessage("Hello Console!");
            }
            return true;
        }

        // command to launch very far
        return false;
    }
}
