package net.nussi.borderXP.shape;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

public abstract class BorderShape {
    private double size = 0.0;

    public void setSize(double size) {
        this.size = size;
    }

    public double getSize() {
        return size;
    }

    public abstract boolean isOutside(Location origin, Location location);

    public boolean isInside(Location origin, Location location) {
        return !isOutside(origin, location);
    }

    public abstract void draw(Location origin, Collection<? extends Player> players);

}
