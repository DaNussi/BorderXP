package net.nussi.borderXP.controller;

import net.nussi.borderXP.shape.BorderShape;
import net.nussi.borderXP.shape.BorderShapeType;
import net.nussi.borderXP.util.LevelCalculator;
import net.nussi.borderXP.util.LevelResult;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Optional;

public class BorderController extends BukkitRunnable implements Listener {
    private final OriginController originController;
    private final BossBarController bossBarController;
    private BorderShape shape = BorderShapeType.SPHERE.getShape();
    private BukkitTask task;

    public BorderController(BossBarController bossBarController, OriginController originController) {
        this.bossBarController = bossBarController;
        this.originController = originController;
    }

    public void onEnable(Plugin plugin) {
        this.task = this.runTaskTimer(plugin, 0, 1);
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);

    }

    public void onDisable() {
        this.task.cancel();
    }

    @Override
    public void run() {

        int rays = 20;
        int steps = 10;
        double distance = 3;

        double angleIncrement = (2 * Math.PI) / rays;
        double stepIncrement = distance / steps;

        for (Player player : Bukkit.getOnlinePlayers()) {
            Location playerLoc = player.getLocation();
            playerLoc.add(new Vector(0.0, 0.1, 0.0));

            Optional<Location> optionalOrigin = originController.getOrCreateOrigin(player);
            if(optionalOrigin.isEmpty()) return;
            Location origin = optionalOrigin.get();
            World world = player.getWorld();

            for (double angle = 0; angle < 2 * Math.PI; angle+=angleIncrement) {
                double x = Math.cos(angle);
                double z = Math.sin(angle);
                Vector directionVec = new Vector(x, 0, z);

                for (double step = 0; step < steps; step+=stepIncrement) {
                    Vector offset = directionVec.clone().multiply(step);
                    Location ray = playerLoc.clone().add(offset);

                    if(shape.isOutside(ray, origin)) {
                        world.spawnParticle(Particle.DUST, ray, 1, new Particle.DustOptions(Color.RED, 0.4f));
                        break;
                    }
                }
            }

        }

    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        Optional<Location> optionalOrigin = originController.getOrCreateOrigin(player);
        if(optionalOrigin.isEmpty()) return;
        Location origin = optionalOrigin.get();
        Location goal = event.getTo();

        if(shape.isOutside(goal, origin))
            player.damage(1);
    }


    public void setShape(BorderShapeType shapeType) {
        this.shape = shapeType.getShape();
    }


    public void onExperienceChange(double xp) {

        LevelResult levelResult = LevelCalculator.calculate(xp);
        int level = levelResult.getLevel();
        double levelProgress = levelResult.getProgress();

        bossBarController.setProgress(level, levelProgress);

        shape.setSize(level + 1);
        Bukkit.getWorlds().forEach(world -> world.getWorldBorder().setSize(level + 128, 3));
    }

    public BorderShape getShape() {
        return shape;
    }
}
