package org.lushplugins.unifiedprotection.bukkit.hook;

import net.william278.cloplib.operation.OperationPosition;
import net.william278.cloplib.operation.OperationType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.unifiedprotection.bukkit.BukkitConverter;
import org.lushplugins.unifiedprotection.hook.AbstractHook;
import org.lushplugins.unifiedprotection.player.OnlinePlayer;

public abstract class AbstractBukkitHook extends AbstractHook {

    public abstract boolean isOperationAllowed(OperationType operationType, Location location, @Nullable Player player);

    @Override
    public boolean isOperationAllowed(OperationType operationType, OperationPosition position, @Nullable OnlinePlayer onlinePlayer) {
        return isOperationAllowed(
            operationType,
            BukkitConverter.convert(position),
            onlinePlayer != null ? BukkitConverter.convert(onlinePlayer) : null);
    }
}
