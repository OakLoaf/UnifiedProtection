package org.lushplugins.unifiedprotection.hook;

import net.william278.cloplib.operation.OperationPosition;
import net.william278.cloplib.operation.OperationType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.unifiedprotection.operation.OperationMappings;
import org.lushplugins.unifiedprotection.player.OnlinePlayer;

public abstract class AbstractHook {
    // TODO: Remove
    private final OperationMappings mappings = new OperationMappings();

    // TODO: Remove
    public OperationMappings getMappings() {
        return mappings;
    }

    // TODO: Remove
    public void setDefaultChecker(@NotNull OperationMappings.OldPositionChecker positionChecker) {
        mappings.setDefaultChecker(positionChecker);
    }

    public abstract boolean isOperationAllowed(OperationType operationType, OperationPosition position, @Nullable OnlinePlayer player);
}
