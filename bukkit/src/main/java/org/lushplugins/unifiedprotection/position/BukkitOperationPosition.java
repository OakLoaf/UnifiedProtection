package org.lushplugins.unifiedprotection.position;

import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public class BukkitOperationPosition extends GenericOperationPosition {

    public BukkitOperationPosition(@NotNull Location location) {
        super(
            location.getX(),
            location.getY(),
            location.getZ(),
            new BukkitOperationWorld(location.getWorld())
        );
    }

    public static class BukkitOperationWorld extends GenericOperationWorld {

        public BukkitOperationWorld(@NotNull World world) {
            super(world.getName(), world.getUID());
        }
    }
}
