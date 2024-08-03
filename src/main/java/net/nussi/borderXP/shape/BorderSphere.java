package net.nussi.borderXP.shape;

import net.nussi.borderXP.util.SphereCalculator;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.List;

public class BorderSphere extends BorderShape {

    @Override
    public boolean isOutside(Location origin, Location location) {
        return origin.distance(location) > this.getSize();
    }

    @Override
    public void draw(Location origin, Collection<? extends Player> players) {
        int points = (int) (calculateSphereArea(this.getSize()) / 20);

        if(points > 200) points = 200;

        List<Vector> positions = SphereCalculator.generatePointsOnSphere(points).stream()
                .map(position -> position.multiply(this.getSize()))
                .toList();

        List<Location> locations = positions.stream()
                .map(position -> origin.clone().add(position))
                .toList();

        locations.forEach(location -> {
            location.getWorld().spawnParticle(Particle.DUST, location, 1, new Particle.DustOptions(Color.WHITE, 0.4f));
        });
    }

    private double calculateSphereArea(double size) {
        return 4 * Math.PI * size * size;
    }

}
