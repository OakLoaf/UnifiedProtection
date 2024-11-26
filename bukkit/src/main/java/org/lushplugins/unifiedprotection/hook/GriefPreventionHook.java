package org.lushplugins.unifiedprotection.hook;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.william278.cloplib.operation.OperationType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.unifiedprotection.utils.ChunkUtils;

public class GriefPreventionHook extends AbstractBukkitHook implements BukkitRegionHook {

    @Override
    public boolean isOperationAllowed(OperationType operationType, Location location, @Nullable Player player) {
        ClaimPermission permission = switch (operationType) {
            case BLOCK_PLACE, BLOCK_BREAK, FARM_BLOCK_PLACE, FARM_BLOCK_BREAK, FARM_BLOCK_INTERACT, FILL_BUCKET,
                 EMPTY_BUCKET, PLACE_HANGING_ENTITY, BREAK_HANGING_ENTITY, USE_SPAWN_EGG, PLAYER_DAMAGE_MONSTER,
                 PLAYER_DAMAGE_ENTITY, PLAYER_DAMAGE_PERSISTENT_ENTITY -> ClaimPermission.Build;
            case CONTAINER_OPEN -> ClaimPermission.Inventory;
            case BLOCK_INTERACT, REDSTONE_INTERACT, ENTITY_INTERACT, ENDER_PEARL_TELEPORT, START_RAID -> ClaimPermission.Access;
            case PLAYER_DAMAGE_PLAYER -> null; // TODO: Verify
            case MONSTER_SPAWN, PASSIVE_MOB_SPAWN -> null; // TODO: Verify
            case MONSTER_DAMAGE_TERRAIN -> null; // TODO: Verify
            case EXPLOSION_DAMAGE_TERRAIN -> null; // TODO: Verify
            case EXPLOSION_DAMAGE_ENTITY -> null; // TODO: Verify
            case FIRE_BURN -> null; // TODO: Verify
            case FIRE_SPREAD -> null; // TODO: Verify
        };

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
    public boolean hasRegionInRange(Location location, int range) {
        DataStore dataStore = GriefPrevention.instance.dataStore;
        return ChunkUtils.getChunksInRange(location, range).stream()
            .anyMatch(chunk -> !dataStore.getClaims(chunk.getX(), chunk.getZ()).isEmpty());
    }

    @Override
    public boolean areRegionsInRangeOwnedBy(Location location, int range, Player player) {
        DataStore dataStore = GriefPrevention.instance.dataStore;
        return ChunkUtils.getChunksInRange(location, range).stream()
            .flatMap(chunk -> dataStore.getClaims(chunk.getX(), chunk.getZ()).stream())
            .allMatch(claim -> claim.checkPermission(player, ClaimPermission.Build, null) == null);
    }
}
