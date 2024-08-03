package net.nussi.borderXP;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.jorel.commandapi.CommandAPICommand;
import net.nussi.borderXP.controller.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class BorderXP extends JavaPlugin {
    private final Logger logger = Bukkit.getLogger();
    private final OriginController originController = new OriginController();
    private final BossBarController bossBarController = new BossBarController();
    private final BorderController borderController = new BorderController(bossBarController, originController);
    private final DrawController drawController = new DrawController(originController, borderController);
    private final ExperienceController experienceController = new ExperienceController(borderController);
    private final CommandController commandController = new CommandController(originController);

    @Override
    public void onEnable() {
        logger.setLevel(Level.INFO);

        originController.onEnable(this);
        bossBarController.onEnable(this);
        experienceController.onEnable(this);
        borderController.onEnable(this);
        drawController.onEnable(this);
        commandController.onEnable(this);

        Bukkit.getLogger().info("Enabled BorderXP!");
    }

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this)
                .verboseOutput(false)
        );

    }

    @Override
    public void onDisable() {
        commandController.onDisable();
        drawController.onDisable();
        borderController.onDisable();
        experienceController.onDisable();
        bossBarController.onDisable();
        originController.onDisable();

        Bukkit.getLogger().info("Disabled BorderXP!");
    }
}
