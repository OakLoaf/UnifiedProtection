package org.lushplugins.unifiedprotection.bukkit.hook;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import net.william278.cloplib.operation.OperationType;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public class WorldGuardHook extends AbstractBukkitHook implements BukkitRegionHook {

    @Override
    public boolean isOperationAllowed(OperationType operationType, Location location, @Nullable Player player) {
        StateFlag flag = switch (operationType) {
            case BLOCK_PLACE, FARM_BLOCK_PLACE -> Flags.BLOCK_PLACE;
            case BLOCK_BREAK, FARM_BLOCK_BREAK -> Flags.BLOCK_BREAK;
            case BLOCK_INTERACT, REDSTONE_INTERACT -> Flags.INTERACT;
            case PLAYER_DAMAGE_PLAYER -> Flags.PVP;
            case PLAYER_DAMAGE_MONSTER, PLAYER_DAMAGE_ENTITY, PLAYER_DAMAGE_PERSISTENT_ENTITY, PLACE_VEHICLE, BREAK_VEHICLE -> Flags.DAMAGE_ANIMALS;
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

    @Override
    public boolean hasRegionWithin(Location loc1, Location loc2) {
        ApplicableRegionSet regionSet = getRegionsInArea(loc1, loc2);
        return regionSet != null && regionSet.size() > 0;
    }

    @Override
    public boolean ownsAllRegionsWithin(Location loc1, Location loc2, Player player) {
        ApplicableRegionSet regionSet = getRegionsInArea(loc1, loc2);
        if (regionSet == null) {
            return true;
        }

        return regionSet.getRegions().stream()
            .allMatch(region -> {
                StateFlag.State state = region.getFlag(Flags.BLOCK_PLACE);
                return (state == null && Flags.BLOCK_PLACE.getDefault() == StateFlag.State.DENY) || state == StateFlag.State.DENY;
            });
    }

    private ApplicableRegionSet getRegionsInArea(Location loc1, Location loc2) {
        RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = regionContainer.get(BukkitAdapter.adapt(loc1.getWorld()));
        if (regionManager == null) {
            return null;
        }

        BlockVector3 min = BlockVector3.at(
            Math.min(loc1.getBlockX(), loc2.getBlockX()),
            Math.min(loc1.getBlockY(), loc2.getBlockY()),
            Math.min(loc1.getBlockZ(), loc2.getBlockZ())
        );
        BlockVector3 max = BlockVector3.at(
            Math.max(loc1.getBlockX(), loc2.getBlockX()),
            Math.max(loc1.getBlockY(), loc2.getBlockY()),
            Math.max(loc1.getBlockZ(), loc2.getBlockZ())
        );

        return regionManager.getApplicableRegions(new ProtectedCuboidRegion("unified-protection-check", true, min, max));
    }
}
