package org.lushplugins.unifiedprotection.hook;

import net.william278.cloplib.operation.Operation;
import net.william278.cloplib.operation.OperationPosition;
import net.william278.cloplib.operation.OperationType;
import net.william278.huskclaims.api.HuskClaimsAPI;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.unifiedprotection.player.OnlinePlayer;

public class HuskClaimsHook extends AbstractHook {

    @Override
    public boolean isOperationAllowed(OperationType operationType, OperationPosition position, @Nullable OnlinePlayer player) {
        HuskClaimsAPI huskClaimsAPI = HuskClaimsAPI.getInstance();
        Operation operation = Operation.of(
            player != null ? huskClaimsAPI.getOnlineUser(player.getUniqueId()) : null,
            operationType,
            position);

        return huskClaimsAPI.isOperationAllowed(operation);
    }
}
