package itzshmulik.survivelist.survivelisttrails;

import itzshmulik.survivelist.survivelisttrails.models.Trail;
import itzshmulik.survivelist.survivelisttrails.models.TrailGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class SurvivelistTrails extends JavaPlugin {
    final QuitListener quitListener = new QuitListener();
    TrailGUI gui;

    @Override
    public void onEnable() {
        // Plugin startup logic
        gui = new TrailGUI(this);

        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(gui, this);
        pluginManager.registerEvents(quitListener, this);
        pluginManager.registerEvents(Trail.CHRISTMAS_2021, this);

        //noinspection ConstantConditions (hides warning bc we know it's in the plugin.yml)
        this.getCommand("trails").setExecutor(new TrailCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        HandlerList.unregisterAll(gui);
        HandlerList.unregisterAll(quitListener);
        HandlerList.unregisterAll(Trail.CHRISTMAS_2021);
    }

    class TrailCommand implements CommandExecutor {
        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
            if(!(sender instanceof Player)){
                sender.sendMessage("You have to be a player to run this command!");
                return true;
            }
            Player player = (Player) sender;
            gui.openInventory(player);
            return true;
        }
    }

    static class QuitListener implements Listener {
        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent e) {
            for (Trail trail : Trail.values()) {
                trail.removeFor(e.getPlayer());
            }
        }
    }
}
