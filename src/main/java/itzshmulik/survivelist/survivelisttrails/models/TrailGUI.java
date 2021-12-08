package itzshmulik.survivelist.survivelisttrails.models;

import com.github.sanctum.templates.Template;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

import static itzshmulik.survivelist.survivelisttrails.util.Util.translate;

public class TrailGUI implements Listener {

    static final int SIZE = 18;
    static final String TITLE = translate("&a&lTrails");
    final JavaPlugin javaPlugin;
    final Template cancelTrailButton = new Template.Builder().setName(translate("&c&lRemove trail")).build();
    final HashMap<Integer, Button> buttons = new HashMap<>();
    final Inventory inv;

    public TrailGUI(JavaPlugin plugin) {
        // Totem Trail
        buttons.put(3, new TotemTrailButton());

        // Fire Trail
        buttons.put(4, new FireTrailButton());

        // Christmas 2021 Trail
        buttons.put(5, new Christmas2021TrailButton());

        // Cancel Trail
        buttons.put(13, new CancelTrailButton());

        inv = generateInventory();

        this.javaPlugin = plugin;
    }

    private Inventory generateInventory() {
        final Inventory inv = Bukkit.createInventory(null, SIZE, TITLE);
        buttons.forEach((index, button) -> inv.setItem(index, button.generate()));
        return inv;
    }

    class TotemTrailButton extends Button {
        @Override
        protected ItemStack generate() {
            return Trail.TOTEM.template.produce(Material.TOTEM_OF_UNDYING);
        }

        @Override
        protected void handle(InventoryClickEvent event) {
            final Player player = (Player) event.getWhoClicked();
            if (player.hasPermission("trails.totem")) {
                //trails.startTotem
                Trail.TOTEM.addFor(player, false);
                // Messages can be processed async just fine
                messageTrailEquipped(player);
            } else {
                messageNoPermission(player);
            }
            // extracted common part
            // We need this to happen on next tick, hence scheduler
            Bukkit.getScheduler().runTask(javaPlugin, player::closeInventory);
        }
    }

    class FireTrailButton extends Button {
        @Override
        protected ItemStack generate() {
            return Trail.FIRE.template.produce(Material.CAMPFIRE);
        }

        @Override
        protected void handle(InventoryClickEvent event) {
            final Player player = (Player) event.getWhoClicked();
            if (player.hasPermission("trails.fire")) {
                //trails.startFire
                Trail.FIRE.addFor(player, false);
                messageTrailEquipped(player);
            } else {
                messageNoPermission(player);
            }
            Bukkit.getScheduler().runTask(javaPlugin, player::closeInventory);
        }
    }

    class Christmas2021TrailButton extends Button {
        @Override
        protected ItemStack generate() {
            return Trail.CHRISTMAS_2021.template.produce(Material.SNOWBALL);
        }

        @Override
        protected void handle(InventoryClickEvent event) {
            final Player player = (Player) event.getWhoClicked();
            if (player.hasPermission("trails.snowball")) {
                //particle.setID(1)
                messageTrailEquipped(player);
            } else {
                messageNoPermission(player);
            }
            Bukkit.getScheduler().runTask(javaPlugin, player::closeInventory);
        }
    }

    class CancelTrailButton extends Button {
        @Override
        protected ItemStack generate() {
            return cancelTrailButton.produce(Material.RED_WOOL);
        }

        @Override
        protected void handle(InventoryClickEvent event) {
            final Player player = (Player) event.getWhoClicked();
            boolean ended = false;
            boolean multipleEnded = false;
            for (Trail trail : Trail.values()) {
                if (trail.removeFor(player)) {
                    if (ended) {
                        multipleEnded = true;
                        continue;
                    }
                    ended = true;
                }
            }
            if (multipleEnded) {
                sendMessageAsync(player, "&c[Survivelist Trails] Removed your trails!");
            } else if (ended) {
                sendMessageAsync(player, "&c[Survivelist Trails] Removed your trail!");
            }
            Bukkit.getScheduler().runTask(javaPlugin, player::closeInventory);
        }
    }

    void sendMessageAsync(Player player, String message) {
        Bukkit.getScheduler().runTaskAsynchronously(javaPlugin, new Messenger(player, message)::send);
    }

    void messageTrailEquipped(Player player) {
        sendMessageAsync(player, "&a&l[Survivelist Trails] Trail equipped!");
    }

    void messageNoPermission(Player player) {
        sendMessageAsync(player, "&cYou have no permission to use that trail!");
    }

    @EventHandler(ignoreCancelled = true)
    public void onClick(InventoryClickEvent e) {
        // only react to our inventory instance
        if (e.getInventory() != inv) return;
        // only react to players
        if (!(e.getWhoClicked() instanceof Player)) return;
        // if they shift click on lower inventory
        if (e.getClickedInventory() == e.getView().getBottomInventory() && e.isShiftClick()) {
            // disallow shift clicks, fast exit
            e.setCancelled(true);
            return;
        }
        // if the clicked on the top inventory
        if (e.getClickedInventory() == e.getInventory()) {
            // cancel by default for top inventory
            e.setCancelled(true);
            // try to get button for the slot from the buttons map
            final Button button = buttons.get(e.getSlot());
            // if slot is a button call handler
            if (button != null) button.handle(e);
        }
    }

    public void openInventory(Player player) {
        player.openInventory(inv);
    }

    static class Messenger {
        final Player player;
        private final String message;

        Messenger(Player player, String message) {
            this.player = player;
            this.message = message;
        }

        void send() {
            player.sendMessage(translate(message));
        }
    }
}
