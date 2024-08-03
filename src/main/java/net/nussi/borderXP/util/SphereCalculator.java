package net.nussi.borderXP.util;

import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class SphereCalculator {


    public static List<Vector> generatePointsOnSphere(int n) {
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
