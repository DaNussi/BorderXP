package net.nussi.borderXP.controller;

import net.nussi.borderXP.shape.BorderShape;
import net.nussi.borderXP.util.SphereCalculator;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.List;

public class DrawController extends BukkitRunnable implements Listener {
    private final OriginController originController;
    private final BorderController borderController;
    private BukkitTask task;

    public DrawController(OriginController originController, BorderController borderController) {
        this.originController = originController;
        this.borderController = borderController;
    }

    public void onEnable(Plugin plugin) {
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);

        this.task = this.runTaskTimer(plugin, 0, 1);
    }

    public void onDisable() {
        this.task.cancel();
    }

    @Override
    public void run() {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();

        BorderShape shape = borderController.getShape();

        for(Location origin : originController.getOrigins()) {

            List<Vector> directions = SphereCalculator.generatePointsOnSphere(40);

            int steps = 10;
            double distance = 3;
            double stepIncrement = distance / steps;


            for(Player player : players) {
                Location playerLoc = player.getLocation();

                boolean isPlayerInside = shape.isInside(origin, playerLoc);

                for (Vector direction : directions) {

                    for (double step = 0; step < steps; step+=stepIncrement) {
                        Vector offset = direction.clone().multiply(step);
                        Location ray = playerLoc.clone().add(offset);

                        boolean check;
                        if(isPlayerInside) {
                            check = shape.isOutside(origin, ray);
                        } else {
                            check = shape.isInside(origin, ray);
                        }

                        if(check) {
                            ray.getWorld().spawnParticle(Particle.DUST, ray, 1, new Particle.DustOptions(Color.RED, 0.4f));
                            break;
                        }
                    }
                }


            }

            borderController.getShape().draw(origin, players);
        }
    }
}
