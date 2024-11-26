package org.lushplugins.unifiedprotection.hook;

import net.william278.cloplib.operation.OperationPosition;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.lushplugins.unifiedprotection.BukkitConverter;
import org.lushplugins.unifiedprotection.player.OnlinePlayer;

public interface BukkitRegionHook extends RegionHook {

    boolean hasRegionInRange(Location location, int range);

    boolean areRegionsInRangeOwnedBy(Location location, int range, Player player);

    @Override
    default boolean hasRegionInRange(OperationPosition position, int range) {
        return hasRegionInRange(
            BukkitConverter.convert(position),
            range
        );
    }

    @Override
    default boolean areRegionsInRangeOwnedBy(OperationPosition position, int range, OnlinePlayer player) {
        return areRegionsInRangeOwnedBy(
            BukkitConverter.convert(position),
            range,
            BukkitConverter.convert(player)
        );
    }
}
