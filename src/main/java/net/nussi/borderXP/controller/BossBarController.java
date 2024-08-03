package net.nussi.borderXP.controller;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class BossBarController implements Listener {
    private BossBar bossBar;


    public void onEnable(Plugin plugin) {
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);

        bossBar = Bukkit.createBossBar("World Border", BarColor.BLUE, BarStyle.SOLID);
        bossBar.setProgress(1.0);
        addAllPlayers();
    }

    public void onDisable() {
        bossBar.removeAll();
    }

    private void addAllPlayers() {
        Bukkit.getOnlinePlayers().forEach(player -> bossBar.addPlayer(player));
    }


    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (bossBar == null) return;
        bossBar.addPlayer(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (bossBar == null) return;
        bossBar.removePlayer(event.getPlayer());
    }

    public void setProgress(double level, double progress) {
        bossBar.setTitle("World Border | " + ((int) level) + " Blocks");
        bossBar.setProgress(progress);
    }

}
