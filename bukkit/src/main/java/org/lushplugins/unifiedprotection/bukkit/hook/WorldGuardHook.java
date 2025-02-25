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
import org.lushplugins.unifiedprotection.utils.ObjectMatcher;

import javax.annotation.Nullable;
import java.util.List;

import static net.william278.cloplib.operation.OperationType.*;

public class WorldGuardHook extends AbstractBukkitHook implements BukkitRegionHook {

    @Override
    public boolean isOperationAllowed(OperationType operationType, Location location, @Nullable Player player) {
        StateFlag flag = ObjectMatcher.onFirstMatch(
            operationType,
            List.of(
                new ObjectMatcher.ObjectRunner<>(
                    () -> Flags.BLOCK_PLACE,
                    BLOCK_PLACE, FARM_BLOCK_PLACE
                ),
                new ObjectMatcher.ObjectRunner<>(
                    () -> Flags.BLOCK_BREAK,
                    BLOCK_BREAK, FARM_BLOCK_BREAK
                ),
                new ObjectMatcher.ObjectRunner<>(
                    () -> Flags.INTERACT,
                    BLOCK_INTERACT, REDSTONE_INTERACT
                ),
                new ObjectMatcher.ObjectRunner<>(
                    () -> Flags.PVP,
                    PLAYER_DAMAGE_PLAYER
                ),
                new ObjectMatcher.ObjectRunner<>(
                    () -> Flags.DAMAGE_ANIMALS,
                    PLAYER_DAMAGE_MONSTER, PLAYER_DAMAGE_ENTITY, PLAYER_DAMAGE_PERSISTENT_ENTITY, PLACE_VEHICLE,
                    BREAK_VEHICLE
                ),
                new ObjectMatcher.ObjectRunner<>(
                    () -> Flags.MOB_SPAWNING,
                    MONSTER_SPAWN, PASSIVE_MOB_SPAWN
                ),
                new ObjectMatcher.ObjectRunner<>(
                    () -> Flags.ENDER_BUILD, // TODO: There are multiple flags for this...
                    MONSTER_DAMAGE_TERRAIN
                ),
                new ObjectMatcher.ObjectRunner<>(
                    () -> Flags.CREEPER_EXPLOSION, // TODO: There are multiple flags for this...
                    EXPLOSION_DAMAGE_TERRAIN
                ),
                new ObjectMatcher.ObjectRunner<>(
                    () -> Flags.FIREWORK_DAMAGE, // TODO: There are multiple flags for this...
                    EXPLOSION_DAMAGE_ENTITY
                ),
                new ObjectMatcher.ObjectRunner<>(
                    () -> Flags.LAVA_FIRE, // TODO: Verify
                    FIRE_BURN
                ),
                new ObjectMatcher.ObjectRunner<>(
                    () -> Flags.FIRE_SPREAD,
                    FIRE_SPREAD
                ),
                new ObjectMatcher.ObjectRunner<>(
                    () -> Flags.BLOCK_BREAK, // TODO: Verify
                    FILL_BUCKET, EMPTY_BUCKET
                ),
                new ObjectMatcher.ObjectRunner<>(
                    () -> Flags.ENTITY_PAINTING_DESTROY, // TODO: There are multiple flags for this...
                    BREAK_HANGING_ENTITY
                ),
                new ObjectMatcher.ObjectRunner<>(
                    () -> Flags.INTERACT, // TODO: Verify
                    FARM_BLOCK_INTERACT
                ),
                new ObjectMatcher.ObjectRunner<>(
                    () -> Flags.MOB_SPAWNING, // TODO: Verify
                    USE_SPAWN_EGG
                ),
                new ObjectMatcher.ObjectRunner<>(
                    () -> Flags.CHORUS_TELEPORT, // TODO: There are multiple flags for this...
                    ENDER_PEARL_TELEPORT
                ),
                new ObjectMatcher.ObjectRunner<>(
                    () -> Flags.INTERACT, // TODO: Verify
                    CONTAINER_OPEN
                ),
                new ObjectMatcher.ObjectRunner<>(
                    () -> Flags.DAMAGE_ANIMALS, // TODO: Verify
                    START_RAID
                )
            )
        );

        if (flag == null) {
            return true;
        }

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
