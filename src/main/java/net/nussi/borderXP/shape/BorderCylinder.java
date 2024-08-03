package net.nussi.borderXP.shape;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;

public class BorderCylinder extends BorderShape{
    @Override
    public boolean isOutside(Location origin, Location location) {
        Location originXZ = origin.clone();
        originXZ.setY(location.getY());

        return originXZ.distance(location) > this.getSize();
    }

    @Override
    public void draw(Location origin, Collection<? extends Player> players) {

    }
}
