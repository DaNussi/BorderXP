package net.nussi.borderXP.controller;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

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

        for(Location origin : originController.getOrigins()) {
            borderController.getShape().draw(origin, players);
        }
    }
}
