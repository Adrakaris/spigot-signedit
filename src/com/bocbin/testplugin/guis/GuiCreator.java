package com.bocbin.testplugin.guis;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GuiCreator {

    public static Inventory createTeamGui() {
        // create inventory

        Inventory inv = Bukkit.createInventory(null, 9, "Change Team");

        // create item
        ItemStack item = new ItemStack(Material.BLUE_CONCRETE);
        ItemMeta meta = item.getItemMeta();

        // blue team
        meta.setDisplayName(ChatColor.BLUE + "Blue Team");
        List<String> lore = new ArrayList<>();
        lore.add(""); lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Click to join team");
        meta.setLore(lore); item.setItemMeta(meta);
        inv.setItem(0, item);  // ARRAYS START AT 0

        // red team
        item.setType(Material.RED_CONCRETE);
        meta.setDisplayName(ChatColor.DARK_RED + "Red Team");
        item.setItemMeta(meta);
        inv.setItem(1, item);

        // yellow team
        item.setType(Material.YELLOW_CONCRETE);
        meta.setDisplayName(ChatColor.GOLD + "Yellow Team");
        item.setItemMeta(meta);
        inv.setItem(2, item);

        // green team
        item.setType(Material.LIME_CONCRETE);
        meta.setDisplayName(ChatColor.GREEN + "Green Team");
        item.setItemMeta(meta);
        inv.setItem(3, item);

        // purple team
        item.setType(Material.PURPLE_CONCRETE);
        meta.setDisplayName(ChatColor.DARK_PURPLE + "Purple Team");
        item.setItemMeta(meta);
        inv.setItem(4, item);

        // team
        item.setType(Material.CYAN_CONCRETE);
        meta.setDisplayName(ChatColor.AQUA + "Aqua Team");
        item.setItemMeta(meta);
        inv.setItem(5, item);

        // team
        item.setType(Material.MAGENTA_CONCRETE);
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Magenta Team");
        item.setItemMeta(meta);
        inv.setItem(6, item);

        // close button
        item.setType(Material.BARRIER);
        meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Close Menu");
        lore.clear();
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(8, item);

        return inv;
    }
}
