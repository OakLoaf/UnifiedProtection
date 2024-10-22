package org.lushplugins.unifiedprotection;

import net.william278.cloplib.operation.OperationPosition;
import net.william278.cloplib.operation.OperationType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.unifiedprotection.hook.*;
import org.lushplugins.unifiedprotection.player.OnlinePlayer;
import org.lushplugins.unifiedprotection.position.BukkitOperationPosition;

import java.util.concurrent.Callable;
import java.util.logging.Level;

public class BukkitUnifiedProtection extends UnifiedProtection {

    public BukkitUnifiedProtection() {
        addPluginHook("HuskClaims", HuskClaimsHook::new);
        addPluginHook("HuskTowns", HuskTownsHook::new);
        addPluginHook("GriefPrevention", GriefPreventionHook::new);
        addPluginHook("WorldGuard", WorldGuardHook::new);
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
    public boolean isOperationAllowed(OperationType operationType, Location location, @Nullable OnlinePlayer player) {
        OperationPosition position = new BukkitOperationPosition(location);
        return isOperationAllowed(operationType, position, player);
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
