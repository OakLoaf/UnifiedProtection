package org.lushplugins.unifiedprotection.hook;

import net.william278.cloplib.operation.OperationPosition;
import org.jetbrains.annotations.ApiStatus;
import org.lushplugins.unifiedprotection.player.OnlinePlayer;

public interface RegionHook {

    boolean hasRegionWithin(OperationPosition pos1, OperationPosition pos2);

    // TODO: Potentially change to checking if operation is allowed
    @ApiStatus.Experimental
    boolean ownsAllRegionsWithin(OperationPosition pos1, OperationPosition pos2, OnlinePlayer player);
}
