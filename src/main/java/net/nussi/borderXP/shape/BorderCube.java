package net.nussi.borderXP.shape;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;

public class BorderCube extends BorderShape {

    @Override
    public boolean isOutside(Location origin, Location location) {
        double side = this.getSize() + 1;
        double halfSide = side / 2.0;

        double originX = origin.getX();
        double originY = origin.getY();
        double originZ = origin.getZ();

        double locationX = location.getX();
        double locationY = location.getY();
        double locationZ = location.getZ();

        return locationX < (originX - halfSide) || locationX > (originX + halfSide) ||
                locationY < (originY - halfSide) || locationY > (originY + halfSide) ||
                locationZ < (originZ - halfSide) || locationZ > (originZ + halfSide);
    }

    @Override
    public void draw(Location origin, Collection<? extends Player> players) {

    }
}
