package itzshmulik.survivelist.survivelisttrails;

import itzshmulik.survivelist.survivelisttrails.Commands.Trail;
import itzshmulik.survivelist.survivelisttrails.Events.ClickEvent;
import itzshmulik.survivelist.survivelisttrails.Events.Movement;
import itzshmulik.survivelist.survivelisttrails.Events.Quit;
import itzshmulik.survivelist.survivelisttrails.Models.GUI;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SurvivelistTrails extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        GUI menu = new GUI();
        menu.register();

        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new ClickEvent(), this);
        pluginManager.registerEvents(new Quit(), this);
        pluginManager.registerEvents(new Movement(), this);

        this.getCommand("trails").setExecutor(new Trail());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
