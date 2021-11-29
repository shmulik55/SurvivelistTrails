package itzshmulik.survivelist.survivelisttrails.Models;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUI {

    private static Inventory INV;

    public void register(){
        Inventory inv = Bukkit.createInventory(null, 18, ChatColor.GREEN + "" + ChatColor.BOLD + "Trails");

        // Totem Trail
        ItemStack item = new ItemStack(Material.TOTEM_OF_UNDYING);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Totem");
        item.setItemMeta(meta);
        inv.setItem(3, item);

        // Fire Trail
        item = new ItemStack(Material.CAMPFIRE);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Fire");
        item.setItemMeta(meta);
        inv.setItem(4, item);

        // Christmas 2021 Trail
        item = new ItemStack(Material.SNOWBALL);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&f&lSnowball (Christmas 2021 special)"));
        item.setItemMeta(meta);
        inv.setItem(5, item);

        // Cancel Trail
        item = new ItemStack(Material.RED_WOOL);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lRemove trail"));
        item.setItemMeta(meta);
        inv.setItem(13, item);

        setInventory(inv);
    }

    public Inventory getInventory(){
        return INV;
    }

    private void setInventory(Inventory inv){
        INV = inv;
    }

    public void openInventory(Player player){
        player.openInventory(INV);
    }
}
