package org.lushplugins.unifiedprotection.hook;

import net.william278.cloplib.operation.OperationPosition;
import net.william278.cloplib.operation.OperationType;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.unifiedprotection.player.OnlinePlayer;

public abstract class AbstractHook {

    public abstract boolean isOperationAllowed(OperationType operationType, OperationPosition position, @Nullable OnlinePlayer player);
}
