package org.lushplugins.unifiedprotection.hook;

import net.william278.cloplib.operation.*;
import net.william278.husktowns.api.HuskTownsAPI;
import net.william278.husktowns.claim.Chunk;
import net.william278.husktowns.claim.TownClaim;
import net.william278.husktowns.claim.World;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.unifiedprotection.player.OnlinePlayer;
import org.lushplugins.unifiedprotection.utils.ChunkUtils;

import java.util.List;
import java.util.UUID;

public class HuskTownsHook extends AbstractHook implements RegionHook {

    @Override
    public boolean isOperationAllowed(OperationType operationType, OperationPosition position, @Nullable OnlinePlayer player) {
        HuskTownsAPI huskTownsAPI = HuskTownsAPI.getInstance();
        Operation operation = Operation.of(
            player != null ? huskTownsAPI.getOnlineUser(player.getUniqueId()) : null,
            operationType,
            position);

        return huskTownsAPI.isOperationAllowed(operation);
    }

    @Override
    public boolean hasRegionWithin(OperationPosition pos1, OperationPosition pos2) {
        HuskTownsAPI huskTownsAPI = HuskTownsAPI.getInstance();
        World world = getWorld(pos1.getWorld());

        return getChunksInArea(pos1, pos2).stream()
            .anyMatch(chunk -> {
                TownClaim claim = huskTownsAPI.getClaimAt(chunk, world).orElse(null);
                return claim != null;
            });
    }

    @Override
    public boolean ownsAllRegionsWithin(OperationPosition pos1, OperationPosition pos2, OnlinePlayer player) {
        HuskTownsAPI huskTownsAPI = HuskTownsAPI.getInstance();
        World world = getWorld(pos1.getWorld());
        UUID uuid = player.getUniqueId();

        return getChunksInArea(pos1, pos2).stream()
            .allMatch(chunk -> {
                TownClaim claim = huskTownsAPI.getClaimAt(chunk, world).orElse(null);
                if (claim == null) {
                    return true;
                }

                return claim.claim().isPlotMember(uuid);
            });
    }

    private List<Chunk> getChunksInArea(OperationPosition pos1, OperationPosition pos2) {
        return ChunkUtils.getChunksInArea(pos1, pos2).stream().map(chunk -> Chunk.at(chunk.getX(), chunk.getZ())).toList();
    }

    private World getWorld(OperationWorld world) {
        return World.of(
            world.getUuid(),
            world.getName(),
            world.getEnvironment()
        );
    }
}
