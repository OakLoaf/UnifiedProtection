package org.lushplugins.unifiedprotection.bukkit.hook;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.william278.cloplib.operation.OperationType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.unifiedprotection.bukkit.utils.ChunkUtils;
import org.lushplugins.unifiedprotection.utils.ObjectMatcher;

import java.util.List;

import static net.william278.cloplib.operation.OperationType.*;

public class GriefPreventionHook extends AbstractBukkitHook implements BukkitRegionHook {

    @Override
    public boolean isOperationAllowed(OperationType operationType, Location location, @Nullable Player player) {
        ClaimPermission permission = ObjectMatcher.onFirstMatch(
            operationType,
            List.of(
                new ObjectMatcher.ObjectRunner<>(
                    () -> ClaimPermission.Build,
                    BLOCK_PLACE, BLOCK_BREAK, FARM_BLOCK_PLACE, FARM_BLOCK_BREAK, FARM_BLOCK_INTERACT, FILL_BUCKET,
                    EMPTY_BUCKET, PLACE_HANGING_ENTITY, BREAK_HANGING_ENTITY, USE_SPAWN_EGG, PLAYER_DAMAGE_MONSTER,
                    PLAYER_DAMAGE_ENTITY, PLAYER_DAMAGE_PERSISTENT_ENTITY, PLACE_VEHICLE, BREAK_VEHICLE
                ),
                new ObjectMatcher.ObjectRunner<>(
                    () -> ClaimPermission.Inventory,
                    CONTAINER_OPEN
                ),
                new ObjectMatcher.ObjectRunner<>(
                    () -> ClaimPermission.Access,
                    BLOCK_INTERACT, REDSTONE_INTERACT, ENTITY_INTERACT, ENDER_PEARL_TELEPORT, START_RAID
                )
            )
        );

        if (permission == null) {
            return true;
        }

        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(
            location,
            false,
            null);

        if (player == null) {
            return claim == null;
        }

        return claim.checkPermission(player, permission, null) == null;
    }

    @Override
    public boolean hasRegionWithin(Location loc1, Location loc2) {
        DataStore dataStore = GriefPrevention.instance.dataStore;
        return ChunkUtils.getChunksInArea(loc1, loc2).stream()
            .anyMatch(chunk -> !dataStore.getClaims(chunk.getX(), chunk.getZ()).isEmpty());
    }

    @Override
    public boolean ownsAllRegionsWithin(Location loc1, Location loc2, Player player) {
        DataStore dataStore = GriefPrevention.instance.dataStore;
        return ChunkUtils.getChunksInArea(loc1, loc2).stream()
            .flatMap(chunk -> dataStore.getClaims(chunk.getX(), chunk.getZ()).stream())
            .allMatch(claim -> claim.checkPermission(player, ClaimPermission.Build, null) == null);
    }
}
