package net.nussi.borderXP.controller;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.LocationType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.Optional;
import java.util.logging.Logger;

public class CommandController implements Listener {
    private final Logger logger = Bukkit.getLogger();
    private final OriginController originController;

    public CommandController(OriginController originController) {
        this.originController = originController;
    }

    public void onEnable(Plugin plugin) {
        CommandAPI.onEnable();

        CommandAPICommand originCommand = new CommandAPICommand("origin")
                .withSubcommand(new CommandAPICommand("set")
                        .withArguments(new LocationArgument("positon", LocationType.PRECISE_POSITION, true))
                        .executes(((sender, args) -> {
                            Optional<Location> optionalOrigin = args.getOptionalByClass("positon", Location.class);
                            if(optionalOrigin.isEmpty()) {
                                sender.sendMessage("You have to specify a position.");
                                return;
                            }
                            Location origin = optionalOrigin.get();
                            origin.add(0,1,0);
                            originController.setOrigin(origin);

                            sender.sendMessage("Set origin of world \"" + origin.getWorld().getName() + "\" to " + origin.toVector());
                        }))
                );

        new CommandAPICommand("border")
                .withSubcommand(originCommand)
                .register();
    }

    public void onDisable() {
        CommandAPI.onDisable();
    }

}
