package itzshmulik.survivelist.survivelisttrails.models;

import com.github.sanctum.templates.Template;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

import static itzshmulik.survivelist.survivelisttrails.util.Util.translate;

public enum Trail implements ManagedTrail, Listener {
    TOTEM(translate("&eTotem"), Particle.TOTEM) {
        final HashMap<UUID, RunnableBasedTrail> runnableTrails = new HashMap<>();

        @Override
        public boolean addFor(Player player, boolean force) {
            synchronized (runnableTrails) {
                final RunnableBasedTrail oldTrail = runnableTrails.get(player.getUniqueId());
                if (force && oldTrail != null) {
                    oldTrail.cancel();
                }
                runnableTrails.put(player.getUniqueId(), new RunnableBasedTrail(particle, player).start());
                return oldTrail == null || !force;
            }
        }

        @Override
        public boolean removeFor(Player player) {
            synchronized (runnableTrails) {
                final RunnableBasedTrail trail = runnableTrails.remove(player.getUniqueId());
                if (trail != null) {
                    trail.cancel();
                    return true;
                }
            }
            return false;
        }
    },
    FIRE(translate("&cFire"), Particle.FLAME) {
        final HashMap<UUID, RunnableBasedTrail> runnableTrails = new HashMap<>();

        @Override
        public boolean addFor(Player player, boolean force) {
            synchronized (runnableTrails) {
                final RunnableBasedTrail oldTrail = runnableTrails.get(player.getUniqueId());
                if (force && oldTrail != null) {
                    oldTrail.cancel();
                }
                runnableTrails.put(player.getUniqueId(), new RunnableBasedTrail(particle, player).start());
                return oldTrail == null || !force;
            }
        }

        @Override
        public boolean removeFor(Player player) {
            synchronized (runnableTrails) {
                final RunnableBasedTrail trail = runnableTrails.remove(player.getUniqueId());
                if (trail != null) {
                    trail.cancel();
                    return true;
                }
            }
            return false;
        }
    },
    CHRISTMAS_2021(translate("&f&lSnowball (Christmas 2021 special)"), Particle.SNOWBALL) {
        final HashSet<UUID> activeTrails = new HashSet<>();
        final AtomicReference<JavaPlugin> plugin = new AtomicReference<>();

        @Override
        public boolean addFor(Player player, boolean force) {
            synchronized (activeTrails) {
                if (!activeTrails.add(player.getUniqueId())) {
                    return force;
                }
            }
            return true;
        }

        @Override
        public boolean removeFor(Player player) {
            synchronized (activeTrails) {
                return activeTrails.remove(player.getUniqueId());
            }
        }

        @EventHandler(ignoreCancelled = true)
        public void onMove(PlayerMoveEvent e) {
            final Location from = e.getFrom();
            final Location to = e.getTo();
            // skip view-only movements
            if (to != null && from.toVector().equals(to.toVector())) return;
//            System.out.println("MOVE EVENT " + e);
            // only target players in hashset
            if (!activeTrails.contains(e.getPlayer().getUniqueId())) return;
//            System.out.println("ACTIVE TARGET " + e.getPlayer() + " Sending loc");
            // immediately send location to async calculation
            processAsync(e.getPlayer().getLocation(), particle);
        }

        void processAsync(Location location, Particle particle) {
            final World world = location.getWorld();
            if (world == null) return;
            final Vector vector = location.toVector();
            Bukkit.getScheduler().runTaskAsynchronously(plugin.updateAndGet(this::getPlugin), () -> {
                for (int i = 0; i < 5; i++) {
                    final double x1, y1, z1;
                    x1 = vector.getX() + ThreadLocalRandom.current().nextDouble(0.5d);
                    y1 = vector.getY() + ThreadLocalRandom.current().nextDouble(0.5d);
                    z1 = vector.getZ() + ThreadLocalRandom.current().nextDouble(0.5d);
                    Bukkit.getScheduler().runTask(plugin.get(), () -> spawn(particle, world, x1, y1, z1));
                    final double x2, y2, z2;
                    x2 = vector.getX() - ThreadLocalRandom.current().nextDouble(0.5d);
                    y2 = vector.getY() + ThreadLocalRandom.current().nextDouble(0.5d);
                    z2 = vector.getZ() - ThreadLocalRandom.current().nextDouble(0.5d);
                    Bukkit.getScheduler().runTask(plugin.get(), () -> spawn(particle, world, x2, y2, z2));
                }
            });
        }

        void spawn(Particle particle, World world, double x, double y, double z) {
            world.spawnParticle(particle, x, y, z, 0);
        }

        JavaPlugin getPlugin(JavaPlugin javaPlugin) {
            if (javaPlugin == null) return JavaPlugin.getProvidingPlugin(getClass());
            return javaPlugin;
        }
    },
    ;

    public final String name;
    public final Template template;
    public final Particle particle;

    Trail(String name, Particle particle) {
        this.name = name;
        this.template = new Template.Builder().setName(name).build();
        this.particle = particle;
    }
}
