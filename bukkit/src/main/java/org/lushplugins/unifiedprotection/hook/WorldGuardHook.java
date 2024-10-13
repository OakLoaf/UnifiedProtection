package org.lushplugins.unifiedprotection.hook;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import net.william278.cloplib.operation.OperationType;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public class WorldGuardHook extends AbstractBukkitHook {

    @Override
    public boolean isOperationAllowed(OperationType operationType, Location location, @Nullable Player player) {
        StateFlag flag = switch (operationType) {
            case BLOCK_PLACE, FARM_BLOCK_PLACE -> Flags.BLOCK_PLACE;
            case BLOCK_BREAK, FARM_BLOCK_BREAK -> Flags.BLOCK_BREAK;
            case BLOCK_INTERACT, REDSTONE_INTERACT -> Flags.INTERACT;
            case PLAYER_DAMAGE_PLAYER -> Flags.PVP;
            case PLAYER_DAMAGE_MONSTER, PLAYER_DAMAGE_ENTITY, PLAYER_DAMAGE_PERSISTENT_ENTITY -> Flags.DAMAGE_ANIMALS;
            case MONSTER_SPAWN, PASSIVE_MOB_SPAWN -> Flags.MOB_SPAWNING;
            case MONSTER_DAMAGE_TERRAIN -> Flags.ENDER_BUILD; // TODO: There are multiple flags for this...
            case EXPLOSION_DAMAGE_TERRAIN -> Flags.CREEPER_EXPLOSION; // TODO: There are multiple flags for this...
            case EXPLOSION_DAMAGE_ENTITY -> Flags.FIREWORK_DAMAGE; // TODO: There are multiple flags for this...
            case FIRE_BURN -> Flags.LAVA_FIRE; // TODO: Verify
            case FIRE_SPREAD -> Flags.FIRE_SPREAD;
            case FILL_BUCKET -> Flags.BLOCK_BREAK; // TODO: Verify
            case EMPTY_BUCKET -> Flags.BLOCK_BREAK; // TODO: Verify
            case PLACE_HANGING_ENTITY -> Flags.BLOCK_PLACE; // TODO: Verify
            case BREAK_HANGING_ENTITY -> Flags.ENTITY_PAINTING_DESTROY; // TODO: There are multiple flags for this...
            case ENTITY_INTERACT -> Flags.INTERACT;
            case FARM_BLOCK_INTERACT -> Flags.INTERACT; // TODO: Verify
            case USE_SPAWN_EGG -> Flags.MOB_SPAWNING; // TODO: Verify
            case ENDER_PEARL_TELEPORT -> Flags.CHORUS_TELEPORT; // TODO: There are multiple flags for this...
            case CONTAINER_OPEN -> Flags.INTERACT; // TODO: Verify
            case START_RAID -> Flags.DAMAGE_ANIMALS; // TODO: Verify
        };

        RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = regionContainer.get(BukkitAdapter.adapt(location.getWorld()));
        if (regionManager == null) {
            return true;
        }

        ApplicableRegionSet applicableRegions = regionManager.getApplicableRegions(BukkitAdapter.adapt(location).toVector().toBlockPoint());
        return applicableRegions.getRegions().stream().noneMatch(region -> {
            StateFlag.State state = region.getFlag(flag);
            return (state == null && flag.getDefault() == StateFlag.State.DENY) || state == StateFlag.State.DENY;
        });
    }
}
