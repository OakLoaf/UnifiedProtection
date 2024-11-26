package org.lushplugins.unifiedprotection.bukkit.position;

import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.lushplugins.unifiedprotection.position.GenericOperationPosition;
import org.lushplugins.unifiedprotection.position.GenericOperationWorld;

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
