package itzshmulik.survivelist.survivelisttrails.Events;

import itzshmulik.survivelist.survivelisttrails.Models.ParticleData;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class Quit implements Listener {

    public void onQuit(PlayerQuitEvent event){
        ParticleData p = new ParticleData(event.getPlayer().getUniqueId());
        if(p.hasID())
            p.endTask();
    }
}
