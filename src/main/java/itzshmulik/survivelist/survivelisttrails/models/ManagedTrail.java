package itzshmulik.survivelist.survivelisttrails.models;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public interface ManagedTrail {
    /**
     * Attempt to add this trail for a player.
     *
     * @param player a player
     * @param force whether to reinitialize the trail if already active
     * @return true only if trail was <i>inactive</i> <b>or</b> if trail
     * was <i>active</i> <u>and</u> <code>force</code> is true
     */
    boolean addFor(Player player, boolean force);

    /**
     * Attempt to remove this trail for a player.
     *
     * @param player a player
     * @return true if trail was active before removal attempt
     */
    boolean removeFor(Player player);

    class RunnableBasedTrail extends BukkitRunnable {
        final JavaPlugin javaPlugin = JavaPlugin.getProvidingPlugin(getClass());
        final Particle particle;
        final Player player;
        double var = 0d;
        final Vector first = new Vector(0d, 0d, 0d);
        final Vector second = new Vector(0d, 0d, 0d);

        RunnableBasedTrail(Particle particle, Player player) {
            this.particle = particle;
            this.player = player;
        }

        @Override
        public void run() {
            // Almost all calc async
            var += Math.PI / 16d;

            // calculate offsets, running heavy calc before sync blocks
            final double cos = Math.cos(var);
            final double sin = Math.sin(var);
            synchronized (first) {
                // Mutate vector
                first.setX(cos).setY(sin + 1).setZ(sin);
            }
            final double cos1 = Math.cos(var + Math.PI);
            final double sin1 = Math.sin(var + Math.PI);
            synchronized (second) {
                second.setX(cos1).setY(sin + 1).setZ(sin1);
            }

            // Trigger read+add and spawn (in-tick/sync)
            Bukkit.getScheduler().runTask(javaPlugin, this::sendSync);
        }

        RunnableBasedTrail start() {
            runTaskTimerAsynchronously(javaPlugin, 0L, 1L);
            return this;
        }

        void sendSync() {
            synchronized (first) {
                player.getWorld().spawnParticle(particle, player.getLocation().add(first), 0);
            }
            synchronized (second) {
                player.getWorld().spawnParticle(particle, player.getLocation().add(second), 0);
            }
        }
    }
}
