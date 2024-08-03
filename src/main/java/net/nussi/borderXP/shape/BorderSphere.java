package net.nussi.borderXP.shape;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BorderSphere extends BorderShape {

    @Override
    public boolean isOutside(Location origin, Location location) {
        return origin.distance(location) > this.getSize();
    }

    @Override
    public void draw(Location origin, Collection<? extends Player> players) {
        int points = (int) (calculateSphereArea(this.getSize()) / 4);

        List<Vector> positions = generatePointsOnSphere(points).stream()
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

    private static List<Vector> generatePointsOnSphere(int n) {
        List<Vector> points = new ArrayList<>(n);
        double phi = Math.PI * (3.0 - Math.sqrt(5.0));  // Golden angle in radians

        for (int i = 0; i < n; i++) {
            double y = 1 - (i / (double)(n - 1)) * 2;  // y goes from 1 to -1
            double radius = Math.sqrt(1 - y * y);      // radius at y

            double theta = phi * i;  // Golden angle increment

            double x = Math.cos(theta) * radius;
            double z = Math.sin(theta) * radius;

            points.add(new Vector(x, y, z));
        }

        return points;
    }
}
