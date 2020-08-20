package com.bocbin.testplugin.components;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ChangeTeam implements CommandExecutor, Listener {

    private Inventory gui;

    public ChangeTeam(Inventory gui) {
        this.gui = gui;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equals("changeteam") || label.equals("changeteams")) {
            if (sender instanceof Player) {
                if (sender.hasPermission("testplugin.changeteam")) {
                    Player player = (Player) sender;
                    // open GUI
                    player.openInventory(gui);
                }
            } else {
                sender.sendMessage("A console cannot use this command");
            }
            return true;
        }

        return false;
    }

    @SuppressWarnings("ConstantConditions")
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!event.getClickedInventory().equals(gui)) { return; }
        if (event.getCurrentItem() == null
                || event.getCurrentItem().getItemMeta() == null
                || event.getCurrentItem().getItemMeta().getDisplayName() == null) {
            return;
        }

        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        // create an array of armour
        ItemStack[] armour = player.getEquipment().getArmorContents();

        switch (event.getSlot()) {
            case 0:
                armour = changeColour(armour, Color.BLUE);
                break;
            case 1:
                armour = changeColour(armour, Color.RED);
                break;
            case 2:
                armour = changeColour(armour, Color.YELLOW);
                break;
            case 3:
                armour = changeColour(armour, Color.LIME);
                break;
            case 4:
                armour = changeColour(armour, Color.PURPLE);
                break;
            case 5:
                armour = changeColour(armour, Color.AQUA);
                break;
            case 6:
                armour = changeColour(armour, Color.FUCHSIA);
                break;
            case 8:
                player.closeInventory();
                return;
            default:
                throw new IllegalStateException("Unexpected value: " + event.getSlot());
        }

        player.getEquipment().setArmorContents(armour);
        player.sendMessage(ChatColor.GOLD + "You changed your team");
    }

    private ItemStack[] changeColour(ItemStack[] armour, Color colour) {
        // change effect in leather armour
        for (ItemStack item : armour) {
            try {
                if (item.getType() == Material.LEATHER_BOOTS
                        || item.getType() == Material.LEATHER_LEGGINGS
                        || item.getType() == Material.LEATHER_CHESTPLATE
                        || item.getType() == Material.LEATHER_HELMET) {

                    LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
                    meta.setColor(colour);
                    item.setItemMeta(meta);
                }
            } catch (Exception e) {
                // nope
            }
        }

        return armour;
    }
}
