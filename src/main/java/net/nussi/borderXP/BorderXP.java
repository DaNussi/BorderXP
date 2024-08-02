package net.nussi.borderXP;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class BorderXP extends JavaPlugin {
    private final BorderController borderController = new BorderController();

    @Override
    public void onEnable() {
        borderController.start();
        borderController.runTaskTimer(this, 0, 1);
        Bukkit.getServer().getPluginManager().registerEvents(borderController, this);
        Bukkit.getServer().getPluginCommand("borderxp").setExecutor(borderController);
        Bukkit.getLogger().info("Enabled BorderXP!");
    }

    @Override
    public void onDisable() {
        borderController.stop();
        Bukkit.getLogger().info("Disabled BorderXP!");
    }
}
