package org.lushplugins.unifiedprotection.bukkit.hook;

import net.william278.cloplib.operation.OperationType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.pvptoggle.api.PvPToggleAPI;

public class PvPToggleHook extends AbstractBukkitHook {

    @Override
    public boolean isOperationAllowed(OperationType operationType, Location location, @Nullable Player player) {
        if (player == null || operationType != OperationType.PLAYER_DAMAGE_PLAYER) {
            return true;
        }

        return PvPToggleAPI.isPvPEnabled(player);
    }
}
