package com.bocbin.testplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GodBoots implements CommandExecutor, Listener {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equals("godboots")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!player.hasPermission("testplugin.godboots")) {
                    player.sendMessage(ChatColor.RED + "You do not have the permission testplugin.godboots!");
                    return true;
                }

                player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "You have been graced by a divine gift");
                // check if inventory empty, drop if not
                if (player.getInventory().firstEmpty() == -1) {  // if there is no empty slot
                    Location location = player.getLocation();
                    World world = player.getWorld();
                    world.dropItemNaturally(location, getItem());  // drop item in world at the location
                    player.sendMessage(ChatColor.GOLD + "" + ChatColor.ITALIC + "Your inventory was full so we dropped it");
                } else {
                    player.getInventory().addItem(getItem());
                }

            } else {
                sender.sendMessage("A console cannot use this command");
            }
            return true;
        }

        return false;
    }

    private ItemStack getItem() {
        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
        ItemMeta meta = boots.getItemMeta();  // creates an item metadata

        assert meta != null;

        meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Boots of Jumpy High");
        List<String> lore = new ArrayList<String>();
        lore.add("");
        lore.add(ChatColor.GOLD + "" + ChatColor.ITALIC + "The boots of the gods. They go very high.");
        meta.setLore(lore);  // setLore takes a List<String>

        meta.addEnchant(Enchantment.PROTECTION_FALL, 4, true);  // just for the shiny glint

        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        meta.setUnbreakable(true);

        boots.setItemMeta(meta);

        return boots;
    }

    // events
    // https://hub.spigotmc.org/javadocs/spigot/org/bukkit/event/entity/package-summary.html
    // https://hub.spigotmc.org/javadocs/spigot/org/bukkit/event/player/package-summary.html
    @EventHandler
    public void onJump(PlayerMoveEvent event) {
        // whenever a player moves
        // there is no jump event, so one has to check if jumping on playermoveevent
        Player player = event.getPlayer();  // we create a player
        if (player.getInventory().getBoots() != null) {  // if player are wearing boots
            if (player.getInventory().getBoots().getItemMeta().getDisplayName().contains("Boots of Jumpy High")) {  // if the name of the boots contain the string we want
                if (player.getInventory().getBoots().getItemMeta().hasLore()) {  // if the boots have lore as well
                    double dy = event.getTo().getY() - event.getFrom().getY();

                    if (dy > 0 && dy != 0.5d // checking if the player is jumping from a solid block but not stairs;
                            && player.getLocation().subtract(0, 1, 0).getBlock().getType() != Material.AIR
                            && player.getLocation().getBlock().getType() != Material.WATER
                            && player.getLocation().subtract(0, 1, 0).getBlock().getType() != Material.WATER) {
                        // the if statement: [1] If the difference in y is <= 1,
                        // AND one block below the player does not equal air (i.e. is a solid [or liquid] block)
                        // now move the player
                        player.setVelocity(player.getLocation().getDirection().multiply(2).setY(2));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onFall(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                if (player.getInventory().getBoots() != null)  // idk you could do that
                    if (player.getInventory().getBoots().getItemMeta().getDisplayName().contains("Boots of Jumpy High"))
                        if (player.getInventory().getBoots().getItemMeta().hasLore()) {
                            event.setCancelled(true);  // cancel fall damage event for player
                        }
            }
        }
    }
}
