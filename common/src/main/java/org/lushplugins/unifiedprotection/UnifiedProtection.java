package org.lushplugins.unifiedprotection;

import net.william278.cloplib.operation.OperationPosition;
import net.william278.cloplib.operation.OperationType;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.unifiedprotection.hook.AbstractHook;
import org.lushplugins.unifiedprotection.player.OnlinePlayer;

import java.util.ArrayList;
import java.util.List;

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

    public void addHook(AbstractHook hook) {
        hooks.add(hook);
    }
}
