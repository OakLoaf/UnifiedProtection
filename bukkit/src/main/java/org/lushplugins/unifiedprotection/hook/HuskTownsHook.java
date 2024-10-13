package org.lushplugins.unifiedprotection.hook;

import net.william278.cloplib.operation.OperationPosition;
import net.william278.cloplib.operation.OperationWorld;
import net.william278.husktowns.api.BukkitHuskTownsAPI;
import net.william278.husktowns.claim.Position;
import net.william278.husktowns.claim.World;
import net.william278.husktowns.libraries.cloplib.operation.Operation;
import net.william278.husktowns.libraries.cloplib.operation.OperationType;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.unifiedprotection.BukkitConverter;
import org.lushplugins.unifiedprotection.player.OnlinePlayer;

public class HuskTownsHook extends AbstractHook {

    @Override
    public boolean isOperationAllowed(net.william278.cloplib.operation.OperationType operationType, OperationPosition position, @Nullable OnlinePlayer onlinePlayer) {
        OperationType type = switch (operationType) {
            case BLOCK_PLACE -> OperationType.BLOCK_PLACE;
            case BLOCK_BREAK -> OperationType.BLOCK_BREAK;
            case BLOCK_INTERACT -> OperationType.BLOCK_INTERACT;
            case REDSTONE_INTERACT -> OperationType.REDSTONE_INTERACT;
            case FARM_BLOCK_BREAK -> OperationType.FARM_BLOCK_BREAK;
            case FARM_BLOCK_PLACE -> OperationType.FARM_BLOCK_PLACE;
            case PLAYER_DAMAGE_PLAYER -> OperationType.PLAYER_DAMAGE_PLAYER;
            case PLAYER_DAMAGE_MONSTER -> OperationType.PLAYER_DAMAGE_MONSTER;
            case PLAYER_DAMAGE_ENTITY -> OperationType.PLAYER_DAMAGE_ENTITY;
            case PLAYER_DAMAGE_PERSISTENT_ENTITY -> OperationType.PLAYER_DAMAGE_PERSISTENT_ENTITY;
            case MONSTER_SPAWN -> OperationType.MONSTER_SPAWN;
            case PASSIVE_MOB_SPAWN -> OperationType.PASSIVE_MOB_SPAWN;
            case MONSTER_DAMAGE_TERRAIN -> OperationType.MONSTER_DAMAGE_TERRAIN;
            case EXPLOSION_DAMAGE_TERRAIN -> OperationType.EXPLOSION_DAMAGE_TERRAIN;
            case EXPLOSION_DAMAGE_ENTITY -> OperationType.EXPLOSION_DAMAGE_ENTITY;
            case FIRE_BURN -> OperationType.FIRE_BURN;
            case FIRE_SPREAD -> OperationType.FIRE_SPREAD;
            case FILL_BUCKET -> OperationType.FILL_BUCKET;
            case EMPTY_BUCKET -> OperationType.EMPTY_BUCKET;
            case PLACE_HANGING_ENTITY -> OperationType.PLACE_HANGING_ENTITY;
            case BREAK_HANGING_ENTITY -> OperationType.BREAK_HANGING_ENTITY;
            case ENTITY_INTERACT -> OperationType.ENTITY_INTERACT;
            case FARM_BLOCK_INTERACT -> OperationType.FARM_BLOCK_INTERACT;
            case USE_SPAWN_EGG -> OperationType.USE_SPAWN_EGG;
            case ENDER_PEARL_TELEPORT -> OperationType.ENDER_PEARL_TELEPORT;
            case CONTAINER_OPEN -> OperationType.CONTAINER_OPEN;
            case START_RAID -> OperationType.START_RAID;
        };

        BukkitHuskTownsAPI huskTownsAPI = BukkitHuskTownsAPI.getInstance();
        Operation operation = Operation.of(
            onlinePlayer != null ? huskTownsAPI.getOnlineUser(BukkitConverter.convert(onlinePlayer)) : null,
            type,
            getPosition(position));

        return huskTownsAPI.isOperationAllowed(operation);
    }

    private Position getPosition(OperationPosition position) {
        return Position.at(
            position.getX(),
            position.getY(),
            position.getZ(),
            getWorld(position.getWorld())
        );
    }

    private World getWorld(OperationWorld world) {
        return World.of(
            world.getUuid(),
            world.getName(),
            world.getEnvironment()
        );
    }
}
