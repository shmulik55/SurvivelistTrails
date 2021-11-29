package itzshmulik.survivelist.survivelisttrails.Commands;

import itzshmulik.survivelist.survivelisttrails.Models.GUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Trail implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equals("trails")){
            if(!(sender instanceof Player)){
                sender.sendMessage("You have to be a player to run this command!");
                return true;
            }
            Player player = (Player) sender;
            GUI menu = new GUI();
            menu.openInventory(player);
            return true;
        }
        return false;
    }
}
