package com.bocbin.SignEdit;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main extends JavaPlugin implements Listener {

    List<Material> signs;

    HashMap<String, Sign> selectedSigns = new HashMap<>();

    public void onEnable() {
        this.signs = makeSignList();
        this.getServer().getPluginManager().registerEvents(this, this);

    }

    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (label.equalsIgnoreCase("se") || label.equalsIgnoreCase("signedit")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("This command must be used by a player");
                return true;
            }

            Player player = (Player) sender;
            String playerName = player.getName();

            // check if player does not have a selected sign
            if (!selectedSigns.containsKey(playerName)) {
                player.sendMessage(ChatColor.RED + "You have not got a selected sign!");
                return true;
            } else if (selectedSigns.get(playerName) == null) {
                player.sendMessage(ChatColor.RED + "You have not got a selected sign!");
                return true;
            }

            // check if player has actually put the command in properly
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Usage: /se <line> <data>");
                return true;
            }

            // then do sign editing
            Sign sign = selectedSigns.get(playerName);

            // convert the line number to an integer, or exit if invalid
            int lineNo;
            try {
                lineNo = Integer.parseInt(args[0]) - 1;
                if (lineNo < 0 || lineNo > 3) {
                    throw new ArrayIndexOutOfBoundsException();
                }
            } catch (Exception e) {
                player.sendMessage(ChatColor.RED + "Error: <line> must be an integer from 1 to 4");
                return true;
            }

            // concatenate the rest of arg into a sentence
            String sentence = "";
            for (int word = 1; word < args.length; word++) {
                sentence += args[word] + " ";
            }
            sentence = sentence.substring(0, sentence.length()-1);

            // set sign
            sign.setLine(lineNo, sentence);  // sets that lne of the sign to the text
            sign.update();  // updates the sign to reflect changes
        }

        return false;
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        // check whether or right click with empty hand
        if (player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (signs.contains(event.getClickedBlock().getType())) {  // if the block clicked on is a sign
                    // checks if the player is already registered, will replace currently selected sign with new sign
                    if (selectedSigns.containsKey(player.getName())) {
                        selectedSigns.replace(player.getName(), (Sign) event.getClickedBlock().getState());
                    } else {
                        selectedSigns.put(player.getName(), (Sign) event.getClickedBlock().getState());  // put the player and their selected sign into the hashmap
                    }

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6You have selected this sign to edit. Use &4/se <line> <data> &6to edit the sign, or click anywhere else to deselect"));

                } else {
                    selectedSigns.replace(player.getName(), null);  // replace key pos with value v
                    // deselect the sign
                }
            }
        }
    }

    public List<Material> makeSignList() {
        List<Material> signout = new ArrayList<>();

        signout.add(Material.OAK_SIGN);
        signout.add(Material.OAK_WALL_SIGN);
        signout.add(Material.BIRCH_SIGN);
        signout.add(Material.BIRCH_WALL_SIGN);
        signout.add(Material.SPRUCE_SIGN);
        signout.add(Material.SPRUCE_WALL_SIGN);
        signout.add(Material.JUNGLE_SIGN);
        signout.add(Material.JUNGLE_WALL_SIGN);
        signout.add(Material.ACACIA_SIGN);
        signout.add(Material.ACACIA_WALL_SIGN);
        signout.add(Material.DARK_OAK_SIGN);
        signout.add(Material.DARK_OAK_WALL_SIGN);
        signout.add(Material.CRIMSON_SIGN);
        signout.add(Material.CRIMSON_WALL_SIGN);
        signout.add(Material.WARPED_SIGN);
        signout.add(Material.WARPED_WALL_SIGN);

        return signout;
    }
}
