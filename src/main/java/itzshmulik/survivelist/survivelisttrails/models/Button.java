package itzshmulik.survivelist.survivelisttrails.models;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public abstract class Button {

    protected abstract ItemStack generate();

    protected abstract void handle(InventoryClickEvent event);

}
