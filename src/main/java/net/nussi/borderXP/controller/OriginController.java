package net.nussi.borderXP.controller;

import net.nussi.borderXP.exception.OriginAlreadyExistsException;
import net.nussi.borderXP.exception.OriginNotFoundException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class OriginController implements Listener {
    private final Logger logger = Bukkit.getLogger();
    public ConcurrentHashMap<UUID, Vector> origins = new ConcurrentHashMap<>();

    public Location getOrigin(Player player) throws OriginNotFoundException {
        World world = player.getWorld();
        Vector position = origins.get(world.getUID());
        if(position == null) throw new OriginNotFoundException("Origin of world \"" + world.getName() + "\" not found!");

        return new Location(world, position.getX(), position.getY(), position.getZ());
    }

    public Location createOrigin(Player player) throws OriginAlreadyExistsException {
        World world = player.getWorld();
        Vector position = origins.get(world.getUID());
        if(position != null) throw new OriginAlreadyExistsException("Origin of world \"" + world.getName() + "\" already exists!");

        Location origin = player.getLocation();
        position = origin.toVector();
        position.add(new Vector(0,0.5,0));

        origins.put(world.getUID(), position);
        world.getWorldBorder().setCenter(origin);


        logger.info("Created origin of world \"" + world.getName() + "\" at " + position);

        return new Location(world, position.getX(), position.getY(), position.getZ());
    }

    public Optional<Location> getOrCreateOrigin(Player player) {
        try {
            return Optional.of(getOrigin(player));
        } catch (OriginNotFoundException exception) {
            logger.warning("Origin not found!");
        }

        try {
            return Optional.of(createOrigin(player));
        } catch (Exception e) {
            logger.severe("Failed to get and create origin!");
            return Optional.empty();
        }
    }


    public void onEnable(Plugin plugin) {
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void onDisable() {

    }


    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        Location respawnLocation = player.getRespawnLocation();
        if(respawnLocation != null) return;

        Optional<Location> optionalOrigin = getOrCreateOrigin(player);
        if(optionalOrigin.isEmpty()) return;
        Location origin = optionalOrigin.get();

        player.setRespawnLocation(origin, true);
        logger.info("Set spawnpoint of player \"" + player.getName() + "\" to origin.");
    }

    public void setOrigin(Location origin) {
        World world = origin.getWorld();
        Vector position = origin.toVector();

        origins.put(world.getUID(), position);
        world.getWorldBorder().setCenter(origin);

        logger.info("Set origin of world \"" + world.getName() + "\" to " + position);
    }

    public List<Location> getOrigins() {
        List<Location> list = new LinkedList<>();

        for(var entry : origins.entrySet()) {
            UUID worldUUID = entry.getKey();
            Vector position = entry.getValue();

            World world = Bukkit.getWorld(worldUUID);
            if(world == null) {
                logger.warning("Failed to find world by UUID. " + worldUUID);
                continue;
            }

            Location origin = new Location(world, position.getX(), position.getY(), position.getZ());
            list.add(origin);
        }

        return list;
    }
}
