package org.lushplugins.unifiedprotection.hook;

import net.william278.cloplib.operation.Operation;
import net.william278.cloplib.operation.OperationPosition;
import net.william278.cloplib.operation.OperationType;
import net.william278.huskclaims.api.HuskClaimsAPI;
import net.william278.huskclaims.claim.Region;
import net.william278.huskclaims.position.Position;
import net.william278.huskclaims.position.World;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.unifiedprotection.player.OnlinePlayer;

import java.util.UUID;

public class HuskClaimsHook extends AbstractHook implements RegionHook {

    @Override
    public boolean isOperationAllowed(OperationType operationType, OperationPosition position, @Nullable OnlinePlayer player) {
        HuskClaimsAPI huskClaimsAPI = HuskClaimsAPI.getInstance();
        Operation operation = Operation.of(
            player != null ? huskClaimsAPI.getOnlineUser(player.getUniqueId()) : null,
            operationType,
            position);

        return huskClaimsAPI.isOperationAllowed(operation);
    }

    @Override
    public boolean hasRegionInRange(OperationPosition position, int range) {
        HuskClaimsAPI huskClaimsAPI = HuskClaimsAPI.getInstance();
        World world = huskClaimsAPI.getWorld(position.getWorld().getName());
        return !huskClaimsAPI.getClaimsOverlapping(world, Region.around(
            Position.at(position.getX(), position.getY(), position.getZ(), world),
            range
        )).isEmpty();
    }

    @Override
    public boolean areRegionsInRangeOwnedBy(OperationPosition position, int range, OnlinePlayer player) {
        HuskClaimsAPI huskClaimsAPI = HuskClaimsAPI.getInstance();
        World world = huskClaimsAPI.getWorld(position.getWorld().getName());
        return huskClaimsAPI.getClaimsOverlapping(world, Region.around(
                Position.at(position.getX(), position.getY(), position.getZ(), world),
                range
            ))
            .stream()
            .allMatch(claim -> {
                UUID uuid = claim.getOwner().orElse(null);
                return uuid != null && player.getUniqueId() == uuid;
            });
    }
}
