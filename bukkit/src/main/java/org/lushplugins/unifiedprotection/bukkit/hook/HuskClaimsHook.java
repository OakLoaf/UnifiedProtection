package org.lushplugins.unifiedprotection.bukkit.hook;

import net.william278.cloplib.operation.OperationPosition;
import net.william278.cloplib.operation.OperationType;
import net.william278.huskclaims.api.HuskClaimsAPI;
import net.william278.huskclaims.claim.Region;
import net.william278.huskclaims.libraries.cloplib.operation.Operation;
import net.william278.huskclaims.position.Position;
import net.william278.huskclaims.position.World;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.unifiedprotection.hook.AbstractHook;
import org.lushplugins.unifiedprotection.hook.RegionHook;
import org.lushplugins.unifiedprotection.player.OnlinePlayer;

import java.util.UUID;

public class HuskClaimsHook extends AbstractHook implements RegionHook {

    @Override
    public boolean isOperationAllowed(OperationType operationType, OperationPosition position, @Nullable OnlinePlayer player) {
        HuskClaimsAPI huskClaimsAPI = HuskClaimsAPI.getInstance();
        Operation operation = Operation.of(
            player != null ? huskClaimsAPI.getOnlineUser(player.getUniqueId()) : null,
            convert(operationType),
            convert(position));

        return huskClaimsAPI.isOperationAllowed(operation);
    }

    @Override
    public boolean hasRegionWithin(OperationPosition pos1, OperationPosition pos2) {
        HuskClaimsAPI huskClaimsAPI = HuskClaimsAPI.getInstance();
        World world = huskClaimsAPI.getWorld(pos1.getWorld().getName());
        return !huskClaimsAPI.getClaimsOverlapping(world, Region.from(
            Position.at(pos1.getX(), pos1.getY(), pos1.getZ(), world),
            Position.at(pos2.getX(), pos2.getY(), pos2.getZ(), world)
        )).isEmpty();
    }

    @Override
    public boolean ownsAllRegionsWithin(OperationPosition pos1, OperationPosition pos2, OnlinePlayer player) {
        HuskClaimsAPI huskClaimsAPI = HuskClaimsAPI.getInstance();
        World world = huskClaimsAPI.getWorld(pos1.getWorld().getName());
        return huskClaimsAPI.getClaimsOverlapping(world, Region.from(
                Position.at(pos1.getX(), pos1.getY(), pos1.getZ(), world),
                Position.at(pos2.getX(), pos2.getY(), pos2.getZ(), world)
            )).stream()
            .allMatch(claim -> {
                UUID uuid = claim.getOwner().orElse(null);
                return uuid != null && player.getUniqueId() == uuid;
            });
    }

    private static net.william278.huskclaims.libraries.cloplib.operation.OperationType convert(OperationType operationType) {
        return net.william278.huskclaims.libraries.cloplib.operation.OperationType.get(operationType.getKey()).orElseThrow();
    }

    private static net.william278.huskclaims.libraries.cloplib.operation.OperationPosition convert(OperationPosition position) {
        return Position.at(
            position.getX(),
            position.getY(),
            position.getZ(),
            position.getYaw(),
            position.getPitch(),
            HuskClaimsAPI.getInstance().getWorld(position.getWorld().getName())
        );
    }
}
