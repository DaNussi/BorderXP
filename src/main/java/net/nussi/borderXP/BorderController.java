package net.nussi.borderXP;

import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class BorderController extends BukkitRunnable implements Listener, CommandExecutor {
    private final HashMap<String, Integer> playerLevels = new HashMap<>();
    private final HashMap<String, Location> origins = new HashMap<>();
    private int currentTotalLevel = 0;
    private BossBar bossBar;

    public void start() {
        bossBar = Bukkit.createBossBar("World Border", BarColor.BLUE, BarStyle.SOLID);
        bossBar.setProgress(1.0);
        Bukkit.getServer().getOnlinePlayers().forEach(player -> bossBar.addPlayer(player));
    }

    public void stop() {
        bossBar.removeAll();
    }

    @Override
    public void run() {

        Bukkit.getOnlinePlayers().forEach(player -> {
            playerLevels.put(player.getUniqueId().toString(), player.getLevel());
        });

        AtomicInteger totalLevelAtomic = new AtomicInteger();
        playerLevels.values().forEach(totalLevelAtomic::addAndGet);
        int newTotalLevel = totalLevelAtomic.get();

        if(currentTotalLevel != newTotalLevel) {
            if(newTotalLevel <= 0) newTotalLevel = 1;

            int borderSize = newTotalLevel + 128;
            int delta = newTotalLevel - currentTotalLevel;
            if(delta < 0) delta *= -1;


            bossBar.setTitle("World Border | " + newTotalLevel + " Blocks");
            bossBar.removeAll();
            Bukkit.getOnlinePlayers().forEach(player -> bossBar.addPlayer(player));

            int deltaFinal = delta;
            Bukkit.getWorlds().forEach(world -> world.getWorldBorder().setSize(borderSize, deltaFinal));
            currentTotalLevel = newTotalLevel;
        }


        int rays = 20;
        int steps = 10;
        double distance = 3;

        double angleIncrement = (2 * Math.PI) / rays;
        double stepIncrement = distance / steps;

        for (Player player : Bukkit.getOnlinePlayers()) {
            Location playerLoc = player.getLocation();
            playerLoc.add(new Vector(0.0, 0.1, 0.0));

            Location origin = getOrigin(player);
            World world = player.getWorld();

            for (double angle = 0; angle < 2 * Math.PI; angle+=angleIncrement) {
                double x = Math.cos(angle);
                double z = Math.sin(angle);
                Vector directionVec = new Vector(x, 0, z);

                for (double step = 0; step < steps; step+=stepIncrement) {
                    Vector offset = directionVec.clone().multiply(step);
                    Location ray = playerLoc.clone().add(offset);

                    if(isOutsideBorder(ray, origin)) {
                        world.spawnParticle(Particle.DUST, ray, 1, new Particle.DustOptions(Color.RED, 0.4f));
                        break;
                    }
                }
            }

        }

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

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        Location origin = getOrigin(player);
        Location goal = event.getTo();

        if(isOutsideBorder(goal, origin))
            player.damage(1);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player player) {
            Location location = player.getLocation();
            location = blocktize(location);

            World world = player.getWorld();
            origins.put(
                    world.getName(),
                    location
            );

            sender.sendMessage("Set origin of world " + world.getName() + " to position " + location.getX() + " " + location.getY() + " " + location.getZ());
            return true;
        } else {
            sender.sendMessage("This command can only used by a player.");
            return false;
        }
    }

    private Location getOrigin(Player player) {
        String worldName = player.getWorld().getName();
        Location origin = origins.get(worldName);
        if(origin == null) {
            origin = blocktize(player.getLocation());
            origins.put(worldName, origin);
        }

        Location originCopy = origin.clone();
        originCopy.setY(player.getLocation().getY());
        return originCopy;
    }

    private Location blocktize(Location location) {
        Location l = location.clone();
        l.setX(l.getBlockX());
        l.setY(l.getBlockY());
        l.setZ(l.getBlockZ());
        return l;
    }

    public boolean isOutsideBorder(Location location, Location origin) {
        double distance = origin.distance(location);
        return distance > currentTotalLevel;
    }

}
