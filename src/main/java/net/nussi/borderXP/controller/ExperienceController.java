package net.nussi.borderXP.controller;

import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ExperienceController extends BukkitRunnable implements Listener {
    private final ConcurrentHashMap<UUID, Double> playerXpMap = new ConcurrentHashMap<>();
    private final AtomicDouble totalXP = new AtomicDouble(0);
    private BukkitTask task;
    private BorderController borderController;

    public ExperienceController(BorderController borderController) {
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
        for(Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            double totalXP = player.getTotalExperience();
            playerXpMap.put(uuid, totalXP);
        }

        double sum = 0;
        for(var entry : playerXpMap.entrySet()) {
            double value = entry.getValue();
            sum += value;
        }

        if(sum != totalXP.get()) {
            totalXP.set(sum);
            borderController.onExperienceChange(sum);
        }

    }
}
