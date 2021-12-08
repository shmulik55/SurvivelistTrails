package itzshmulik.survivelist.survivelisttrails.util;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.Contract;

public class Util {
    private Util() {}

    @Contract("null -> null")
    public static String translate(String text) {
        if (text == null) return null;
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
