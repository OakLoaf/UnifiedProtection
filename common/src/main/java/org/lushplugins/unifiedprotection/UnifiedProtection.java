package org.lushplugins.unifiedprotection;

import net.william278.cloplib.operation.OperationPosition;
import net.william278.cloplib.operation.OperationType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.unifiedprotection.hook.AbstractHook;
import org.lushplugins.unifiedprotection.hook.RegionHook;
import org.lushplugins.unifiedprotection.player.OnlinePlayer;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class UnifiedProtection {
    protected final List<AbstractHook> hooks = new ArrayList<>();

    protected UnifiedProtection() {}

    /**
     * @param operationType The operation type to check
     * @param position The position to check
     * @return Whether an operation is allowed at a given location
     */
    public boolean isOperationAllowed(OperationType operationType, OperationPosition position) {
        return isOperationAllowed(operationType, position, null);
    }

    /**
     * @param operationType The operation type to check
     * @param position The position to check
     * @param player The player to check
     * @return Whether an operation is allowed by a player at a given location
     */
    public boolean isOperationAllowed(OperationType operationType, OperationPosition position, @Nullable OnlinePlayer player) {
        for (AbstractHook hook : hooks) {
            if (!hook.isOperationAllowed(operationType, position, player)) {
                return false;
            }
        }

        return true;
    }

    /**
     * @param position The center position
     * @param range The range around the position
     * @return Whether a region is within range of the center position
     */
    public boolean hasRegionInRange(OperationPosition position, int range) {
        for (AbstractHook hook : hooks) {
            if (hook instanceof RegionHook regionHook) {
                if (regionHook.hasRegionInRange(position, range)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * @param position The center position
     * @param range The range around the position
     * @param player The player to check
     * @return Whether all regions within range of the center position are owned by the player
     */
    @ApiStatus.Experimental
    public boolean areRegionsInRangeOwnedBy(OperationPosition position, int range, OnlinePlayer player) {
        for (AbstractHook hook : hooks) {
            if (hook instanceof RegionHook regionHook) {
                if (!regionHook.areRegionsInRangeOwnedBy(position, range, player)) {
                    return false;
                }
            }
        }

        return true;
    }

    public void addHook(AbstractHook hook) {
        hooks.add(hook);
    }
}
