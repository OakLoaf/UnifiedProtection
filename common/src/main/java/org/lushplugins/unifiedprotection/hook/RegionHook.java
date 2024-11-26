package org.lushplugins.unifiedprotection.hook;

import net.william278.cloplib.operation.OperationPosition;
import org.lushplugins.unifiedprotection.player.OnlinePlayer;

public interface RegionHook {

    boolean hasRegionInRange(OperationPosition position, int range);

    // TODO: Potentially change to checking if operation is allowed
    boolean areRegionsInRangeOwnedBy(OperationPosition position, int range, OnlinePlayer player);
}
