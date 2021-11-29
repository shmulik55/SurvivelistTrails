package itzshmulik.survivelist.survivelisttrails.Events;

import itzshmulik.survivelist.survivelisttrails.Models.Effects;
import itzshmulik.survivelist.survivelisttrails.Models.GUI;
import itzshmulik.survivelist.survivelisttrails.Models.ParticleData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;

public class ClickEvent implements Listener {

    private GUI menu;
    public ClickEvent(){
        menu = new GUI();
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(!event.getInventory().equals(menu.getInventory()))
            return;

        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);

        if(event.getView().getType() == InventoryType.PLAYER)
            return;

        ParticleData particle = new ParticleData(player.getUniqueId());

        if(particle.hasID()){
            particle.endTask();
            particle.removeID();
        }

        Effects trails = new Effects(player);

        switch(event.getSlot()){
            case 3:
                if(player.hasPermission("trails.totem")){
                    trails.startTotem();
                    player.closeInventory();
                    player.updateInventory();
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&a&l[Survivelist Trails] Trail equipped!"));
                }
                else{
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have no permission to use that trail!"));
                    player.closeInventory();
                    player.updateInventory();
                }
                break;
            case 4:
                if(player.hasPermission("trails.fire")){
                    trails.startFire();
                    player.closeInventory();
                    player.updateInventory();
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&a&l[Survivelist Trails] Trail equipped!"));
                }else{
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have no permission to use that command!"));
                }
                break;
            case 5:
                if(player.hasPermission("trails.snowball")){
                    particle.setID(1);
                    player.closeInventory();
                    player.updateInventory();
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&a&l[Survivelist Trails] Trail equipped!"));
                }else{
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have no permission to use that command!"));
                }

            case 13:
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c[Survivelist Trails] Removed your trail!"));
                player.closeInventory();
                player.updateInventory();
                break;
            default:
                break;
        }
    }
}
