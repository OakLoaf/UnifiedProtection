package org.lushplugins.unifiedprotection.bukkit;

import net.william278.cloplib.operation.OperationPosition;
import net.william278.cloplib.operation.OperationType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.unifiedprotection.UnifiedProtection;
import org.lushplugins.unifiedprotection.bukkit.hook.GriefPreventionHook;
import org.lushplugins.unifiedprotection.bukkit.hook.HuskTownsHook;
import org.lushplugins.unifiedprotection.bukkit.hook.PvPToggleHook;
import org.lushplugins.unifiedprotection.bukkit.hook.WorldGuardHook;
import org.lushplugins.unifiedprotection.hook.*;
import org.lushplugins.unifiedprotection.player.OnlinePlayer;
import org.lushplugins.unifiedprotection.bukkit.position.BukkitOperationPosition;

import java.util.concurrent.Callable;
import java.util.logging.Level;

public class BukkitUnifiedProtection extends UnifiedProtection {

    @SuppressWarnings("Convert2MethodRef")
    public BukkitUnifiedProtection() {
        addPluginHook("GriefPrevention", () -> new GriefPreventionHook());
        addPluginHook("HuskClaims", () -> new HuskClaimsHook());
        addPluginHook("HuskTowns", () -> new HuskTownsHook());
        addPluginHook("PvPToggle", () -> new PvPToggleHook());
        addPluginHook("WorldGuard", () -> new WorldGuardHook());
    }

    /**
     * @param operationType The operation type to check
     * @param location The location to check
     * @return Whether an operation is allowed at a given location
     */
    public boolean isOperationAllowed(OperationType operationType, Location location) {
        return isOperationAllowed(operationType, location, null);
    }

    /**
     * @param operationType The operation type to check
     * @param location The location to check
     * @param player The player to check
     * @return Whether an operation is allowed by a player at a given location
     */
    public boolean isOperationAllowed(OperationType operationType, Location location, @Nullable Player player) {
        OperationPosition position = new BukkitOperationPosition(location);
        OnlinePlayer onlinePlayer = BukkitConverter.convert(player);
        return isOperationAllowed(operationType, position, onlinePlayer);
    }

    /**
     * @param location The center location
     * @param range The range around the location
     * @return Whether a region is within range of the center location
     */
    public boolean hasRegionInRange(Location location, int range) {
        OperationPosition position = new BukkitOperationPosition(location);
        return hasRegionInRange(position, range);
    }

    /**
     * @param location The center location
     * @param range The range around the location
     * @param player The player to check
     * @return Whether all regions within range of the center location are owned by the player
     */
    @ApiStatus.Experimental
    public boolean areRegionsInRangeOwnedBy(Location location, int range, Player player) {
        OperationPosition position = new BukkitOperationPosition(location);
        OnlinePlayer onlinePlayer = BukkitConverter.convert(player);
        return areRegionsInRangeOwnedBy(position, range, onlinePlayer);
    }

    private void addPluginHook(String pluginName, Callable<AbstractHook> hook) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
        if (plugin != null && plugin.isEnabled()) {
            try {
                addHook(hook.call());
            } catch (Exception e) {
                Bukkit.getLogger().log(Level.WARNING, "[UnifiedProtection] Error whilst loading hook: ", e);
            }
        }
    }
}
