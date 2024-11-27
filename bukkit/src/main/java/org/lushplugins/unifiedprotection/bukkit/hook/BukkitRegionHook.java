package org.lushplugins.unifiedprotection.bukkit.hook;

import net.william278.cloplib.operation.OperationPosition;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.lushplugins.unifiedprotection.bukkit.BukkitConverter;
import org.lushplugins.unifiedprotection.hook.RegionHook;
import org.lushplugins.unifiedprotection.player.OnlinePlayer;

public interface BukkitRegionHook extends RegionHook {

    boolean hasRegionWithin(Location loc1, Location loc2);

    boolean ownsAllRegionsWithin(Location loc1, Location loc2, Player player);

    @Override
    default boolean hasRegionWithin(OperationPosition pos1, OperationPosition pos2) {
        return hasRegionWithin(
            BukkitConverter.convert(pos1),
            BukkitConverter.convert(pos2)
        );
    }

    @Override
    default boolean ownsAllRegionsWithin(OperationPosition pos1, OperationPosition pos2, OnlinePlayer player) {
        return ownsAllRegionsWithin(
            BukkitConverter.convert(pos1),
            BukkitConverter.convert(pos2),
            BukkitConverter.convert(player)
        );
    }
}
