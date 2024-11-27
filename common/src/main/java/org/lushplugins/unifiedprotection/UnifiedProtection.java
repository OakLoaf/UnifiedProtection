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
     * @param pos1 The first position
     * @param pos2 The second position
     * @return Whether a region is within the specified area
     */
    public boolean hasRegionWithin(OperationPosition pos1, OperationPosition pos2) {
        for (AbstractHook hook : hooks) {
            if (hook instanceof RegionHook regionHook) {
                if (regionHook.hasRegionWithin(pos1, pos2)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * @param pos1 The first position
     * @param pos2 The second position
     * @param player The player to check
     * @return Whether all regions within the specified area are owned by the player
     */
    @ApiStatus.Experimental
    public boolean ownsAllRegionsWithin(OperationPosition pos1, OperationPosition pos2, OnlinePlayer player) {
        for (AbstractHook hook : hooks) {
            if (hook instanceof RegionHook regionHook) {
                if (!regionHook.ownsAllRegionsWithin(pos1, pos2, player)) {
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
