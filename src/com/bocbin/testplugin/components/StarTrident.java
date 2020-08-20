package com.bocbin.testplugin.components;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class StarTrident implements CommandExecutor, Listener {

    public List<String> list = new ArrayList<>();

    /*
    Spawns a special trident item on command /startrident
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equals("startrident")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!player.hasPermission("testplugin.startrident")) {
                    player.sendMessage(ChatColor.RED + "You do not have the permission testplugin.startrident!");
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

    /*
    Helper function to spawn the trident
     */
    private ItemStack getItem() {
        ItemStack trident = new ItemStack(Material.TRIDENT);
        ItemMeta meta = trident.getItemMeta();  // creates an item metadata

        assert meta != null;

        meta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "The Star Trident");
        List<String> lore = new ArrayList<String>();
        lore.add("");
        lore.add(ChatColor.AQUA + "" + ChatColor.ITALIC + "Forged with the fury of stars and seas");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&dLeft click to shoot a fireball"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&5Right click and throw to summon mobs"));
        meta.setLore(lore);  // setLore takes a List<String>

        meta.addEnchant(Enchantment.MENDING, 1, true);  // just for the shiny glint

        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);

        trident.setItemMeta(meta);

        return trident;
    }

    /*
    Event function which checks for a player interaction with air, whilst holding the trident
    Onl eft click, summon fireball.
    On right click, will add the player to a list of "activated players", this is used for trident hit
     */
    @EventHandler()
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getInventory().getItemInMainHand().getType().equals(Material.TRIDENT))
            if (player.getInventory().getItemInMainHand().getItemMeta().hasLore()) {
                // Right click
                if (event.getAction() == Action.RIGHT_CLICK_AIR) {
                    // throwing trident
                    if (!list.contains(player.getName())) {
                        list.add(player.getName());  // want to separate our trident from other tridents
                        // so creates a list so that whenever someone rclicks on this trident,
                        // they are added to the very cool list and can spawn zombies
                    }
                    return;
                }

                // left click
                if (event.getAction() == Action.LEFT_CLICK_AIR) {
                    player.launchProjectile(Fireball.class);
                    // https://hub.spigotmc.org/javadocs/spigot/org/bukkit/entity/Projectile.html
                }
            }
        if (list.contains(player.getName())) {
            list.remove(player.getName());
        }
    }

    /*
    On trident hit, if shot by a player in the activated cool list,
    will summon 3 drowneds in a 2 block radius around the trident collision point.
     */
    @EventHandler()
    public void onTridentLand(ProjectileHitEvent event) {
        // see what entity type it is
        if (event.getEntityType() == EntityType.TRIDENT)
            if (event.getEntity().getShooter() instanceof Player) {
                Player player = (Player) event.getEntity().getShooter();
                if (list.contains(player.getName())) {
                    // spawn drowned zombies
                    Location loc = event.getEntity().getLocation();
                    loc.setY(loc.getY() + 1);
                    for (int i = 0; i < 3; i++) {  // spawn three drownedb
                        loc.getWorld().spawnEntity(loc, EntityType.DROWNED);
                        loc.setX(loc.getX() + ThreadLocalRandom.current().nextInt(-2, 3));
                        loc.setZ(loc.getZ() + ThreadLocalRandom.current().nextInt(-2, 3));
                    }
                }
            }
    }
}
